package com.kasunjay.miigrasbackend.model.web;

import com.kasunjay.miigrasbackend.common.enums.GradientType;
import lombok.Data;

@Data
public class GradientDTO {
    private long id;
    private GradientType gradientType;
    private PersonDTO person;

    private Boolean sameAsEmployeeAddress;
}
