package com.kasunjay.miigrasbackend.model.web;

import lombok.Data;

@Data
public class DashboardDTO {
    private long sosCount;
    private long newSosCount;
    private long complaintCount;
    private long newComplaintCount;
    private long employeeCount;
    private long newEmployeeCount;
    private long messageCount;
    private long newMessageCount;
}
