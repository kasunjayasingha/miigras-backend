package com.kasunjay.miigrasbackend.entity.web;

import com.kasunjay.miigrasbackend.common.enums.PersonTypes;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "person_type")
@Data
public class PersonType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PersonTypes type;

    @OneToMany(mappedBy = "personType")
    private List<Person> persons;

    @OneToMany(mappedBy = "personType")
    private List<Gradient> gradients;



}
