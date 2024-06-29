package com.kasunjay.miigrasbackend.entity.web;

import com.kasunjay.miigrasbackend.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "house_no")
    private String houseNumber;

    @Column(name = "street_one")
    private String streetOne;

    @Column(name = "street_two")
    private String streetTwo;

    private String village;

    @NotNull(message = "City is required")
    @Column(nullable = false)
    private String city;

    private String district;

    @Column(name = "postal_code")
    private String postalCode;

    @OneToOne(mappedBy = "addressAgency")
    private Agency agency;

}
