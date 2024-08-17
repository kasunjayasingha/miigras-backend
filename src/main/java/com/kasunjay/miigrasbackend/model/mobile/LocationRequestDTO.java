package com.kasunjay.miigrasbackend.model.mobile;

import lombok.Data;

@Data
public class LocationRequestDTO {
    private long employeeId;
    private double latitude;
    private double longitude;
}
