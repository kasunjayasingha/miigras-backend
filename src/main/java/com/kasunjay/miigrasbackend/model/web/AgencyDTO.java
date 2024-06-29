package com.kasunjay.miigrasbackend.model.web;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AgencyDTO {
    private long id;
    private String email;
    private String fax;
    private String name;
    private String phone;
    private String phone2;
    private String regNum;
    private AddressDTO addressAgency;
    private DomainMinistryDTO domainMinistry;
}
