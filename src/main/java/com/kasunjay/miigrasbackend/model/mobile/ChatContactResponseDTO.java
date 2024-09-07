package com.kasunjay.miigrasbackend.model.mobile;

import lombok.Data;

import java.util.List;

@Data
public class ChatContactResponseDTO {
    private List<ChatContactDTO> data;
}
