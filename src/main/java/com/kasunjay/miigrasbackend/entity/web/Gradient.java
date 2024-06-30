package com.kasunjay.miigrasbackend.entity.web;

import com.kasunjay.miigrasbackend.common.enums.GradientType;
import com.kasunjay.miigrasbackend.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Gradient extends BaseEntity {

    @Column(name = "gradient_type")
    @Enumerated(EnumType.STRING)
    private GradientType gradientType;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    private Person person;

    @OneToOne(mappedBy = "gradient")
    private Employee employee;

}
