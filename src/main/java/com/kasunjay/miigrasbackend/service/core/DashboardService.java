package com.kasunjay.miigrasbackend.service.core;

import com.kasunjay.miigrasbackend.model.web.DashboardDTO;
import com.kasunjay.miigrasbackend.model.web.IncidentDTO;

import java.util.List;

public interface DashboardService {
    DashboardDTO getTilesValues();

    List<IncidentDTO> getIncidents();
}
