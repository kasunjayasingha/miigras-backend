package com.kasunjay.miigrasbackend.service.core;

import com.kasunjay.miigrasbackend.entity.web.Country;

import java.util.List;

public interface MainService {
    void saveCountry(Country country);

    List<Country> getCountryList();
}
