package com.kasunjay.miigrasbackend.model.web;

import com.kasunjay.miigrasbackend.model.mobile.PredictionDTO;
import lombok.Data;

import java.util.List;

@Data
public class IncidentDashBoardDTO {
    private long id;
    private String employeeName;
    private String employeeCountry;
    private String employeeJobTitle;
    private String serverity;
    private String incidentDate;
    private List<PredictionDTO> todayPrediction;
    private List<PredictionDTO> lastPrediction;
}
