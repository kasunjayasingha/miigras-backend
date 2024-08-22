package com.kasunjay.miigrasbackend.model.web;

import com.kasunjay.miigrasbackend.model.mobile.EmployeeTrackingDTO;
import com.kasunjay.miigrasbackend.model.mobile.PredictionDTO;
import lombok.Data;

@Data
public class IncidentDTO {
    private long id;
    private EmployeeDTO employee;
    private EmployeeTrackingDTO employeeTracking;
    private PredictionDTO prediction;
}
