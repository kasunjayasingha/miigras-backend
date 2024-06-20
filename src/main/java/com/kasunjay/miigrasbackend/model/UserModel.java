package com.kasunjay.miigrasbackend.model;

import lombok.Data;

@Data
public class UserModel {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String matchingPassword;
}
