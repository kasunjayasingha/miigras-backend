package com.kasunjay.miigrasbackend.model.mobile;

import com.kasunjay.miigrasbackend.common.enums.Emotion;
import com.kasunjay.miigrasbackend.common.enums.Level;
import com.kasunjay.miigrasbackend.model.web.EmployeeDTO;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class PredictionDTO {
    private double score;
    private Level severity;
    private String message;
    private Emotion emotion;
    private double emotionScore;
    private EmployeeDTO employee;
}
