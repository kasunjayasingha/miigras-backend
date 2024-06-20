package com.kasunjay.miigrasbackend.entity.web;

import com.kasunjay.miigrasbackend.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "domain_ministry")
@Data
public class DomainMinistry extends BaseEntity {
    private Long id;

    private String email;

    private String fax;

    private String name;

    private String phone;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "domainMinistry")
    private List<Agency> agencies;
}
