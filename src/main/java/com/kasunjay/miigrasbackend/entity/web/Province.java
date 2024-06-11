package com.kasunjay.miigrasbackend.entity.web;

import com.kasunjay.miigrasbackend.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Province extends BaseEntity {

    private String name;

    private String description;

}
