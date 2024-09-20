package com.kasunjay.miigrasbackend.model.web;

import lombok.Data;

@Data
public class FirebaseNotificationDTO {
    private String title;
    private String body;
    private String fcmToken;
}
