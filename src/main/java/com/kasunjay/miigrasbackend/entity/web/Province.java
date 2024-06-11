package com.kasunjay.miigrasbackend.entity.web;

import com.kasunjay.miigrasbackend.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Province extends BaseEntity {

    private String name;

    private String description;

    @OneToMany(mappedBy = "province")
    private List<Location> locations;

}
