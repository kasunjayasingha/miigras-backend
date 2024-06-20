package com.kasunjay.miigrasbackend.common.mapper;

import com.kasunjay.miigrasbackend.entity.web.DomainMinistry;
import com.kasunjay.miigrasbackend.model.DomainMinistryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MainMapper {

    @Mapping(target = "country.id", source = "country.id")
    DomainMinistry domainMinistryDTOToDomainMinistry(DomainMinistryDTO domainMinistryDTO);
}
