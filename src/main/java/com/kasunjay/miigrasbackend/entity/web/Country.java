package com.kasunjay.miigrasbackend.entity.web;

import com.kasunjay.miigrasbackend.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Country extends BaseEntity {
    private String code;

    @Column(unique = true)
    private String name;

    @Column(name="ntp_time")
    private String ntpTime;

    @OneToMany(mappedBy = "country")
    private List<CountryHasProvince> countryHasProvinces;

}
