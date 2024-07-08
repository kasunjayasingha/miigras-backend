package com.kasunjay.miigrasbackend.entity.mobile;

import com.kasunjay.miigrasbackend.common.enums.Emotion;
import com.kasunjay.miigrasbackend.common.enums.Level;
import com.kasunjay.miigrasbackend.entity.BaseEntity;
import com.kasunjay.miigrasbackend.entity.web.Employee;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Prediction extends BaseEntity {
    private double score;

    private Level severity;

    @Column(unique = true, nullable = false,length = 1000)
    private String message;

    private Emotion emotion;

    @Column(name = "emotion_score")
    private double emotionScore;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
