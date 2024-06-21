package com.kasunjay.miigrasbackend.model.web;

import lombok.Data;

@Data
public class DomainMinistryDTO {
    private Long id;
    private String email;
    private String fax;
    private String name;
    private String phone;
    private CountryDTO country;
}
