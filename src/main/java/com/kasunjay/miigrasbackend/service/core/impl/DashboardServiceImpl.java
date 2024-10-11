package com.kasunjay.miigrasbackend.service.core.impl;

import com.kasunjay.miigrasbackend.common.exception.DashboardException;
import com.kasunjay.miigrasbackend.common.mapper.DashboardMapper;
import com.kasunjay.miigrasbackend.common.mapper.MainMapper;
import com.kasunjay.miigrasbackend.entity.mobile.EmployeeTracking;
import com.kasunjay.miigrasbackend.entity.mobile.Prediction;
import com.kasunjay.miigrasbackend.model.web.DashboardDTO;
import com.kasunjay.miigrasbackend.model.web.IncidentDTO;
import com.kasunjay.miigrasbackend.model.web.IncidentDashBoardDTO;
import com.kasunjay.miigrasbackend.repository.EmployeeRepo;
import com.kasunjay.miigrasbackend.repository.EmployeeTrackingRepo;
import com.kasunjay.miigrasbackend.repository.PredictionRepo;
import com.kasunjay.miigrasbackend.repository.SOSRepo;
import com.kasunjay.miigrasbackend.service.core.DashboardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Override
    public DashboardDTO getTilesValues() {
        try{
            log.info("DashboardServiceImpl.getTilesValues");
            LocalDateTime currentDate = LocalDateTime.now();
            LocalDateTime oneMonthBefore = currentDate.minusWeeks(1);

            DashboardDTO dashboardDTO = new DashboardDTO();
            dashboardDTO.setEmployeeCount(employeeRepo.count());
            dashboardDTO.setNewEmployeeCount(employeeRepo.countEmployeeByCreatedDateBetween(oneMonthBefore, currentDate));

            dashboardDTO.setComplaintCount(predictionRepo.count());
            dashboardDTO.setNewComplaintCount(predictionRepo.countPredictionByCreatedDateBetween(oneMonthBefore, currentDate));

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
            Long id = 0L;
            for (Prediction prediction : predictions) {
                IncidentDashBoardDTO incidentDashBoardDTO = new IncidentDashBoardDTO();
                incidentDashBoardDTO.setId(prediction.getId());
                incidentDashBoardDTO.setEmployeeName(prediction.getEmployee().getPerson().getFirstName() + " " + prediction.getEmployee().getPerson().getLastName());
                incidentDashBoardDTO.setEmployeeCountry(String.valueOf(prediction.getEmployee().getJobType()));
                incidentDashBoardDTO.setServerity(String.valueOf(prediction.getSeverity()));

                incidentDashBoardDTOList.add(incidentDashBoardDTO);
            }
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
}
