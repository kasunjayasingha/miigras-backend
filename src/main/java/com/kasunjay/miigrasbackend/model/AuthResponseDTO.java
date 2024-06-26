package com.kasunjay.miigrasbackend.model;

import com.kasunjay.miigrasbackend.common.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private Long id;
    private Roles role;
    private String accessToken;
}
