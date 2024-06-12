package com.kasunjay.miigrasbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
public class User extends BaseEntity {

    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;

    private String mobile;

    @Column(length = 60)
    @Transient
    private String password;

    private String role;
    private boolean enabled = false;
}
