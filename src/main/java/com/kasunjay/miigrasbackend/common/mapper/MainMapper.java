package com.kasunjay.miigrasbackend.common.mapper;

import com.kasunjay.miigrasbackend.entity.mobile.EmployeeTracking;
import com.kasunjay.miigrasbackend.entity.web.*;
import com.kasunjay.miigrasbackend.model.mobile.EmployeeTrackingDTO;
import com.kasunjay.miigrasbackend.model.web.*;
import org.mapstruct.*;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MainMapper {

    @Mapping(target = "country.id", source = "country.id")
    DomainMinistry domainMinistryDTOToDomainMinistry(DomainMinistryDTO domainMinistryDTO);

    Country countryDTOToCountry(CountryDTO countryDTO);

    @Named("domainMinistryToDomainMinistryDTO")
    DomainMinistryDTO domainMinistryToDomainMinistryDTO(DomainMinistry domainMinistry);

    List<DomainMinistryDTO> mapToDomainMinistryDTOList(List<DomainMinistry> domainMinistries);

    Address addressDTOToAddress(AddressDTO addressDTO);

    @Mapping(target = "domainMinistry.id", source = "domainMinistry.id")
    @Mapping(target = "addressAgency", ignore = true)
    Agency agencyDTOToAgency(AgencyDTO agencyDTO);

    List<AgencyDTO> mapToAgencyDTOList(List<Agency> agencies);

    @Mapping(target = "address", ignore = true)
    Person personDTOToPerson(PersonDTO personDTO);

    @Mapping(target = "person", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "gradient", ignore = true)
    @Mapping(target = "agency.id", source = "agency.id")
    Employee employeeDTOToEmployee(EmployeeDTO employeeDTO);

    @Mapping(target = "person", ignore = true)
    Gradient gradientDTOToGradient(GradientDTO gradientDTO);

    @Named("employeeToEmployeeDTO")
    @Mapping(target = "user", ignore = true)
    EmployeeDTO employeeToEmployeeDTO(Employee employee);

    @IterableMapping(qualifiedByName = "employeeToEmployeeDTO")
    List<EmployeeDTO> mapToEmployeeDTOList(List<Employee> employees);

    @Mapping(target = "employee", ignore = true)
    EmployeeTrackingDTO employeeTrackingToEmployeeTrackingDTO(EmployeeTracking employeeTracking);
}
