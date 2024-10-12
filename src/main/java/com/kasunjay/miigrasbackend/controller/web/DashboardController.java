package com.kasunjay.miigrasbackend.controller.web;


import com.kasunjay.miigrasbackend.common.enums.Success;
import com.kasunjay.miigrasbackend.entity.web.Country;
import com.kasunjay.miigrasbackend.model.StandardResponse;
import com.kasunjay.miigrasbackend.model.web.DashboardDTO;
import com.kasunjay.miigrasbackend.model.web.IncidentDTO;
import com.kasunjay.miigrasbackend.model.web.IncidentDashBoardDTO;
import com.kasunjay.miigrasbackend.service.core.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@Slf4j
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/getTilesData")
    public ResponseEntity<DashboardDTO> getTilesValues() {
        log.info("DashboardController.getTilesValues");
        return new ResponseEntity<>(dashboardService.getTilesValues(), HttpStatus.OK);
    }

    @GetMapping("/getIncidentsData")
    public ResponseEntity<List<IncidentDashBoardDTO>> getIncidents() {
//        log.info("DashboardController.getIncidents");
        return new ResponseEntity<>(dashboardService.getIncidents(), HttpStatus.OK);
    }

    @GetMapping("/getIncidentDataById/{id}")
    public ResponseEntity<IncidentDTO> getIncidentsById(@PathVariable Long id) {
        log.info("DashboardController.getIncidentsById");
        return new ResponseEntity<>(dashboardService.getIncidentsById(id), HttpStatus.OK);
    }

    @PostMapping("/complete-incident")
    public ResponseEntity<StandardResponse> completeIncident(@RequestBody IncidentDTO incidentDTO) {
        log.info("DashboardController.completeIncident");
        dashboardService.completeIncident(incidentDTO);
        return new ResponseEntity<>(new StandardResponse(HttpStatus.OK, Success.SUCCESS, "Incident completed"), HttpStatus.OK);
    }
}
