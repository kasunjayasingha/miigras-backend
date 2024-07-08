package com.kasunjay.miigrasbackend.entity.web;

import com.kasunjay.miigrasbackend.common.enums.JobTypes;
import com.kasunjay.miigrasbackend.entity.BaseEntity;
import com.kasunjay.miigrasbackend.entity.User;
import com.kasunjay.miigrasbackend.entity.mobile.Prediction;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Employee extends BaseEntity {

    @Column(name="emp_id")
    private String empId;

    private Boolean status = true;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private Person person;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type")
    private JobTypes jobType;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "gradient_id")
    private Gradient gradient;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Prediction> predictions;
}
