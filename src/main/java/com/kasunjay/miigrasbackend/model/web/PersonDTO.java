package com.kasunjay.miigrasbackend.model.web;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PersonDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String nic;
    private String email;
    private String mobile1;
    private String mobile2;
    private String passport;
    private String dob;
    private AddressDTO address;
}
