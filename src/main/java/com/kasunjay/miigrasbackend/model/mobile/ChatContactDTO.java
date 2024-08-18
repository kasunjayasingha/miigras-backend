package com.kasunjay.miigrasbackend.model.mobile;

import com.kasunjay.miigrasbackend.common.enums.JobTypes;
import com.kasunjay.miigrasbackend.entity.mobile.EmployeeTracking;
import lombok.Data;

import java.util.List;

@Data
public class ChatContactDTO {
    private Long employeeId;
    private String name;
    private String email;
    private String phone;
    private String profilePic;
    private JobTypes jobType;
    private double latitude;
    private double longitude;
}
