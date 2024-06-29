package com.kasunjay.miigrasbackend.common.mapper;

import com.kasunjay.miigrasbackend.entity.web.Address;
import com.kasunjay.miigrasbackend.entity.web.Agency;
import com.kasunjay.miigrasbackend.entity.web.Country;
import com.kasunjay.miigrasbackend.entity.web.DomainMinistry;
import com.kasunjay.miigrasbackend.model.web.AddressDTO;
import com.kasunjay.miigrasbackend.model.web.AgencyDTO;
import com.kasunjay.miigrasbackend.model.web.CountryDTO;
import com.kasunjay.miigrasbackend.model.web.DomainMinistryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

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
}
