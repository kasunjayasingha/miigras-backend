package com.kasunjay.miigrasbackend.entity.web;

import com.kasunjay.miigrasbackend.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Agency extends BaseEntity {

    private String email;

    private String fax;

    private String name;

    @Column(name="phone_1")
    private String phone;

    @Column(name="phone_2")
    private String phone2;

    @Column(name="reg_num")
    private String regNum;

    private Boolean status;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address addressAgency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "domain_ministry_id")
    private DomainMinistry domainMinistry;

    @OneToMany(mappedBy = "agency")
    private List<Employee> employees;
}
