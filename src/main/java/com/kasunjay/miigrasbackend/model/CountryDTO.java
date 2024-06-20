package com.kasunjay.miigrasbackend.model;
import lombok.Data;

@Data
public class CountryDTO {
    private Long id;
    private String code;
    private String name;
    private String ntpTime;
}
