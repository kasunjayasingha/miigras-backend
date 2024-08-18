package com.kasunjay.miigrasbackend.entity.mobile;

import com.kasunjay.miigrasbackend.entity.BaseEntity;
import com.kasunjay.miigrasbackend.entity.web.Employee;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "employee_realtime_location")
public class EmployeeTracking extends BaseEntity {

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;
}
