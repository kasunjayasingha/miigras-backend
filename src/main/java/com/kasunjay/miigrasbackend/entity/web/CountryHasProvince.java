package com.kasunjay.miigrasbackend.entity.web;

import com.kasunjay.miigrasbackend.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CountryHasProvince extends BaseEntity {

    private boolean status;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "province_id")
    private Province province;
}
