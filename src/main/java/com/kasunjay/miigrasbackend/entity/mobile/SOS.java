package com.kasunjay.miigrasbackend.entity.mobile;

import com.kasunjay.miigrasbackend.entity.BaseEntity;
import com.kasunjay.miigrasbackend.entity.web.Employee;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "sos")
public class SOS extends BaseEntity {

    private Boolean active = true;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
