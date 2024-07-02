package com.kasunjay.miigrasbackend.service.core;

import com.kasunjay.miigrasbackend.entity.web.Country;
import com.kasunjay.miigrasbackend.model.StandardResponse;
import com.kasunjay.miigrasbackend.model.web.AgencyDTO;
import com.kasunjay.miigrasbackend.model.web.CountryDTO;
import com.kasunjay.miigrasbackend.model.web.DomainMinistryDTO;
import com.kasunjay.miigrasbackend.model.web.EmployeeDTO;

import java.util.List;

public interface MainService {
    void saveCountry(CountryDTO country);

    List<Country> getCountryList();

    void saveDomainMinistry(DomainMinistryDTO domainMinistryDTO);

    StandardResponse checkCountryIsPresent(String name, String code);

    void deleteCountry(Long id);

    List<DomainMinistryDTO> getDomainMinistryList();

    void saveAgency(AgencyDTO agencyDTO);

    List<AgencyDTO> getAgencyList();

    void saveEmployee(EmployeeDTO employeeDTO);

    List<EmployeeDTO> getEmployeeList();

    String generateEmployeId();
}
