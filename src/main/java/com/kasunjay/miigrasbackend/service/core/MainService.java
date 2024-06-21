package com.kasunjay.miigrasbackend.service.core;

import com.kasunjay.miigrasbackend.entity.web.Country;
import com.kasunjay.miigrasbackend.model.web.CountryDTO;
import com.kasunjay.miigrasbackend.model.web.DomainMinistryDTO;

import java.util.List;

public interface MainService {
    void saveCountry(CountryDTO country);

    List<Country> getCountryList();

    void saveDomainMinistry(DomainMinistryDTO domainMinistryDTO);
}
