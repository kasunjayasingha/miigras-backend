package com.kasunjay.miigrasbackend.model.web;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressDTO {
    private long id;
    private String houseNumber;
    private String streetOne;
    private String streetTwo;
    private String village;
    private String city;
    private String district;
    private String postalCode;
}
