package com.kasunjay.miigrasbackend.service.core.impl;

import com.kasunjay.miigrasbackend.common.exception.DashboardException;
import com.kasunjay.miigrasbackend.common.mapper.DashboardMapper;
import com.kasunjay.miigrasbackend.common.mapper.MainMapper;
import com.kasunjay.miigrasbackend.common.util.AutherizedUserService;
import com.kasunjay.miigrasbackend.entity.mobile.EmployeeTracking;
import com.kasunjay.miigrasbackend.entity.mobile.Prediction;
import com.kasunjay.miigrasbackend.model.StandardResponse;
import com.kasunjay.miigrasbackend.model.mobile.PredictionDTO;
import com.kasunjay.miigrasbackend.model.web.DashboardDTO;
import com.kasunjay.miigrasbackend.model.web.FirebaseNotificationDTO;
import com.kasunjay.miigrasbackend.model.web.IncidentDTO;
import com.kasunjay.miigrasbackend.model.web.IncidentDashBoardDTO;
import com.kasunjay.miigrasbackend.repository.EmployeeRepo;
import com.kasunjay.miigrasbackend.repository.EmployeeTrackingRepo;
import com.kasunjay.miigrasbackend.repository.PredictionRepo;
import com.kasunjay.miigrasbackend.repository.SOSRepo;
import com.kasunjay.miigrasbackend.service.core.DashboardService;
import com.kasunjay.miigrasbackend.service.core.MobileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final EmployeeRepo employeeRepo;
    private final PredictionRepo predictionRepo;
    private final EmployeeTrackingRepo employeeTrackingRepo;
    private final MainMapper mainMapper;
    private final DashboardMapper dashboardMapper;
    private final SOSRepo sosRepo;
    private final MobileService mobileService;

    @Override
    public DashboardDTO getTilesValues() {
        try{
            log.info("DashboardServiceImpl.getTilesValues");
            LocalDateTime currentDate = LocalDateTime.now();
            LocalDateTime oneMonthBefore = currentDate.minusWeeks(1);

            DashboardDTO dashboardDTO = new DashboardDTO();
            dashboardDTO.setEmployeeCount(employeeRepo.count());
            dashboardDTO.setNewEmployeeCount(employeeRepo.countEmployeeByCreatedDateBetween(oneMonthBefore, currentDate));

            dashboardDTO.setComplaintCount(predictionRepo.countPredictionByIsCheckFalse());
            dashboardDTO.setNewComplaintCount(predictionRepo.countPredictionByCreatedDateBetweenAndIsCheckFalse(oneMonthBefore, currentDate));

            dashboardDTO.setSosCount(sosRepo.count());
            dashboardDTO.setNewSosCount(sosRepo.countSOSByCreatedDateBetween(oneMonthBefore, currentDate));
            return dashboardDTO;

        }catch (Exception e){
            log.error("Error in DashboardServiceImpl.getTilesValues: ", e);
            throw new DashboardException( e.getMessage());
        }
    }

    @Override
    public List<IncidentDashBoardDTO> getIncidents() {
        try {
//            log.info("DashboardServiceImpl.getIncidents");
            List<Prediction> predictions = predictionRepo.findByIsCheckFalseOrderByScoreDescCreatedDateDesc();
            if(predictions.isEmpty()){
                return null;
            }
            List<IncidentDashBoardDTO> incidentDashBoardDTOList = new ArrayList<>();
            List<PredictionDTO> todayPredictionList = new ArrayList<>();
            List<PredictionDTO> lastPredictionList = new ArrayList<>();
            LocalDateTime currentDate = LocalDateTime.now();
            for (Prediction prediction : predictions) {
                IncidentDashBoardDTO incidentDashBoardDTO = new IncidentDashBoardDTO();
                incidentDashBoardDTO.setId(prediction.getId());
                incidentDashBoardDTO.setEmployeeName(prediction.getEmployee().getPerson().getFirstName() + " " + prediction.getEmployee().getPerson().getLastName());
                incidentDashBoardDTO.setEmployeeCountry(String.valueOf(prediction.getEmployee().getJobType()));
                incidentDashBoardDTO.setServerity(String.valueOf(prediction.getSeverity()));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                incidentDashBoardDTO.setIncidentDate(prediction.getCreatedDate().format(formatter));

                PredictionDTO predictionDTO;
                if(prediction.getCreatedDate().toLocalDate().isEqual(currentDate.toLocalDate())) {
                    predictionDTO = dashboardMapper.predictionToPredictionDTO(prediction);
                    predictionDTO.setName(prediction.getEmployee().getPerson().getFirstName() + " " + prediction.getEmployee().getPerson().getLastName());
                    todayPredictionList.add(predictionDTO);
                }else {
                    predictionDTO = dashboardMapper.predictionToPredictionDTO(prediction);
                    predictionDTO.setName(prediction.getEmployee().getPerson().getFirstName() + " " + prediction.getEmployee().getPerson().getLastName());
                    lastPredictionList.add(predictionDTO);
                }
                incidentDashBoardDTOList.add(incidentDashBoardDTO);
            }
            incidentDashBoardDTOList.get(0).setTodayPrediction(todayPredictionList);
            incidentDashBoardDTOList.get(0).setLastPrediction(lastPredictionList);
            return incidentDashBoardDTOList;
        } catch (Exception e) {
            log.error("Error in DashboardServiceImpl.getIncidents: ", e);
            throw new DashboardException(e.getMessage());
        }
    }

    @Override
    public IncidentDTO getIncidentsById(Long id) {
        try {
            log.info("DashboardServiceImpl.getIncidentsById");
            Optional<Prediction> prediction = predictionRepo.findById(id);
            if(prediction.isEmpty()){
                return null;
            }
            IncidentDTO incidentDTO = new IncidentDTO();
            incidentDTO.setId(prediction.get().getId());
            incidentDTO.setEmployee(mainMapper.employeeToEmployeeDTO(prediction.get().getEmployee()));
            incidentDTO.setPrediction(dashboardMapper.predictionToPredictionDTO(prediction.get()));
            incidentDTO.setEmployeeTracking(dashboardMapper.employeeTrackingToEmployeeTrackingDTO(employeeTrackingRepo.findByEmployeeAndIsAvailable(prediction.get().getEmployee(), true).orElse(null)));
            return incidentDTO;

        } catch (Exception e) {
            log.error("Error in DashboardServiceImpl.getIncidentsById: ", e);
            throw new DashboardException(e.getMessage());
        }
    }

    @Override
    public void completeIncident(IncidentDTO incidentDTO) {
        try {
            log.info("DashboardServiceImpl.completeIncident");
            Optional<Prediction> prediction = predictionRepo.findById(incidentDTO.getPrediction().getId());
            if(prediction.isEmpty()){
                throw new DashboardException("Incident not found");
            }
            prediction.get().setIsCheck(true);
            prediction.get().setUpdatedBy(AutherizedUserService.getAutherizedUser().getUsername());
            predictionRepo.save(prediction.get());

            FirebaseNotificationDTO firebaseNotificationDTO = new FirebaseNotificationDTO();
            firebaseNotificationDTO.setFcmToken(incidentDTO.getEmployee().getFcmToken());
            firebaseNotificationDTO.setTitle("Complaint");
            firebaseNotificationDTO.setBody("Your complaint has been resolved");
            mobileService.sendNotification(firebaseNotificationDTO);
        } catch (Exception e) {
            log.error("Error in DashboardServiceImpl.completeIncident: ", e);
            throw new DashboardException(e.getMessage());
        }
    }
}
