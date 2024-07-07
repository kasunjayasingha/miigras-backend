package com.kasunjay.miigrasbackend.model.web;

import com.kasunjay.miigrasbackend.common.enums.JobTypes;
import com.kasunjay.miigrasbackend.model.UserModel;
import lombok.Data;

@Data
public class EmployeeDTO {
    private long id;
    private String empId;
    private PersonDTO person;
    private UserModel user;
    private AgencyDTO agency;
    private GradientDTO gradient;
    private JobTypes jobType;
}
