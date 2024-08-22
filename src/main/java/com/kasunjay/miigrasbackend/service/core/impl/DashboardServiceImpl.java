package com.kasunjay.miigrasbackend.service.core.impl;

import com.kasunjay.miigrasbackend.common.exception.DashboardException;
import com.kasunjay.miigrasbackend.common.mapper.DashboardMapper;
import com.kasunjay.miigrasbackend.common.mapper.MainMapper;
import com.kasunjay.miigrasbackend.entity.mobile.EmployeeTracking;
import com.kasunjay.miigrasbackend.entity.mobile.Prediction;
import com.kasunjay.miigrasbackend.model.web.DashboardDTO;
import com.kasunjay.miigrasbackend.model.web.IncidentDTO;
import com.kasunjay.miigrasbackend.repository.EmployeeRepo;
import com.kasunjay.miigrasbackend.repository.EmployeeTrackingRepo;
import com.kasunjay.miigrasbackend.repository.PredictionRepo;
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

    @Override
    public DashboardDTO getTilesValues() {
        try{
            log.info("DashboardServiceImpl.getTilesValues");
            LocalDateTime currentDate = LocalDateTime.now();
            LocalDateTime oneMonthBefore = currentDate.minusWeeks(1);

            DashboardDTO dashboardDTO = new DashboardDTO();
            dashboardDTO.setEmployeeCount(employeeRepo.count());
            dashboardDTO.setNewEmployeeCount(employeeRepo.countEmployeeByCreatedDateBetween(oneMonthBefore, currentDate));
            return dashboardDTO;

        }catch (Exception e){
            log.error("Error in DashboardServiceImpl.getTilesValues: ", e);
            throw new DashboardException( e.getMessage());
        }
    }

    @Override
    public List<IncidentDTO> getIncidents() {
        try {
            log.info("DashboardServiceImpl.getIncidents");
            List<Prediction> predictions = predictionRepo.findByIsCheckFalseOrderByScoreDesc();
            if(predictions.isEmpty()){
                return null;
            }
            List<IncidentDTO> incidentDTOList = new ArrayList<>();
            Long id = 0L;
            for (Prediction prediction : predictions) {
                IncidentDTO incidentDTO = new IncidentDTO();
                incidentDTO.setId(id++);
                incidentDTO.setEmployee(mainMapper.employeeToEmployeeDTO(prediction.getEmployee()));
                incidentDTO.setPrediction(dashboardMapper.predictionToPredictionDTO(prediction));
                incidentDTO.setEmployeeTracking(dashboardMapper.employeeTrackingToEmployeeTrackingDTO(employeeTrackingRepo.findByEmployeeAndIsAvailable(prediction.getEmployee(), true).orElse(null)));
                incidentDTOList.add(incidentDTO);
            }
            return incidentDTOList;
        } catch (Exception e) {
            log.error("Error in DashboardServiceImpl.getIncidents: ", e);
            throw new DashboardException(e.getMessage());
        }
    }
}
