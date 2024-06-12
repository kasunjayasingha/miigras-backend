package com.kasunjay.miigrasbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Created by is mandatory")
    @Column(nullable = false, name = "created_by")
    @Transient
    private String createdBy;

    @Transient
    @Column(name = "updated_by")
    private String updatedBy;

    @CreationTimestamp
    @Column(updatable = false)
    @Transient
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Transient
    private LocalDateTime updatedDate;
}
