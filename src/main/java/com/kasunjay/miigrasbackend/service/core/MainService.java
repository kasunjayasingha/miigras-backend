package com.kasunjay.miigrasbackend.service.core;

import com.kasunjay.miigrasbackend.entity.web.Country;
import com.kasunjay.miigrasbackend.model.DomainMinistryDTO;

public interface MainService {
    void saveCountry(Country country);

    void saveDomainMinistry(DomainMinistryDTO domainMinistryDTO);
}
