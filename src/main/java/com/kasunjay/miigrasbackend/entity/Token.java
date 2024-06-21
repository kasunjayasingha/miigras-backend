package com.kasunjay.miigrasbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(updatable = false)
    @Transient
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Transient
    private LocalDateTime updatedDate;

    private String username;

    @Column(unique = true, nullable = false,length = 1000)
    private String accessToken;
    private boolean expired;
    private boolean revoked;

    public Token(String accessToken, String username) {
        this.accessToken = accessToken;
        this.username = username;
        this.revoked = false;
        this.expired = false;
    }
}
