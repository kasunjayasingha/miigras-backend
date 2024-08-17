package com.kasunjay.miigrasbackend.model.mobile;

import com.kasunjay.miigrasbackend.entity.web.Employee;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class EmployeeTrackingDTO {
    private double latitude;
    private double longitude;
    private Employee employee;
}
