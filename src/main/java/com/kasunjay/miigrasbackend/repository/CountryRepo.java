package com.kasunjay.miigrasbackend.repository;

import com.kasunjay.miigrasbackend.entity.web.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepo extends JpaRepository<Country, Long> {
    Boolean existsByName(String name);

    Boolean existsByCode(String code);
}
