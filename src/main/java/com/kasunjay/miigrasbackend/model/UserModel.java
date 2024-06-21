package com.kasunjay.miigrasbackend.model;

import com.kasunjay.miigrasbackend.common.enums.Roles;
import lombok.Data;

@Data
public class UserModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String matchingPassword;
    private Roles role;
}
