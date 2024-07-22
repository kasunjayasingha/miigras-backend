package com.kasunjay.miigrasbackend.service.core.impl;

import com.kasunjay.miigrasbackend.common.exception.DashboardException;
import com.kasunjay.miigrasbackend.model.web.DashboardDTO;
import com.kasunjay.miigrasbackend.repository.EmployeeRepo;
import com.kasunjay.miigrasbackend.service.core.DashboardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final EmployeeRepo employeeRepo;

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
}
