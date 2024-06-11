package com.kasunjay.miigrasbackend.entity.web;

import com.kasunjay.miigrasbackend.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Person extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @NotNull(message = "NIC is mandatory")
    @Column(nullable = false)
    private String nic;

    @NotNull(message = "Email is mandatory")
    @Column(nullable = false)
    private String email;

    @NotNull(message = "Mobile 1 is mandatory")
    @Column(name = "mobile_1", nullable = false)
    private String mobile1;

    @Column(name = "mobile_2")
    private String mobile2;

    @NotNull(message = "Passport is mandatory")
    @Column(nullable = false)
    private String passport;

    private String dob;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;


}
