package com.kasunjay.miigrasbackend.model.web;

import lombok.Data;

@Data
public class IncidentDashBoardDTO {
    private long id;
    private String employeeName;
    private String employeeCountry;
    private String employeeJobTitle;
    private String serverity;
}
