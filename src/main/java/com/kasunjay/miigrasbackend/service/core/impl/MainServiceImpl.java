package com.kasunjay.miigrasbackend.service.core.impl;

import com.kasunjay.miigrasbackend.common.exception.MainServiceException;
import com.kasunjay.miigrasbackend.common.mapper.MainMapper;
import com.kasunjay.miigrasbackend.common.util.AutherizedUserService;
import com.kasunjay.miigrasbackend.entity.web.Country;
import com.kasunjay.miigrasbackend.entity.web.DomainMinistry;
import com.kasunjay.miigrasbackend.model.web.CountryDTO;
import com.kasunjay.miigrasbackend.model.web.DomainMinistryDTO;
import com.kasunjay.miigrasbackend.repository.CountryRepo;
import com.kasunjay.miigrasbackend.repository.DomainMinistryRepo;
import com.kasunjay.miigrasbackend.service.core.MainService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final CountryRepo countryRepo;
    private final DomainMinistryRepo domainMinistryRepo;
    private final MainMapper mainMapper;

    @Override
    public void saveCountry(CountryDTO countryDTO) {
        try {
            if(countryDTO.getCode() == null || countryDTO.getName() == null || countryDTO.getNtpTime() == null){
                log.error("MainServiceImpl.saveCountry.country is null");
                throw new MainServiceException("Country is null");
            }
            // Save country
            Country country = mainMapper.countryDTOToCountry(countryDTO);
            country.setCreatedBy("ADMIN");
            countryRepo.save(country);
            log.info("MainServiceImpl.saveCountry.country saved: {}", country.getName());
        }catch (Exception e){
            e.printStackTrace();
            throw new MainServiceException(e.getMessage());
        }
    }

    @Override
    public List<Country> getCountryList() {
        try {
            List<Country> countries = countryRepo.findAll();
            log.info("MainServiceImpl.getCountry.countries: {}", countries.size());
            return countries;
        }catch (Exception e){
            e.printStackTrace();
            throw new MainServiceException(e.getMessage());
        }
    }

    @Override
    public void saveDomainMinistry(DomainMinistryDTO domainMinistryDTO) {
        try{
            log.info("MainServiceImpl.saveDomainMinistry.called");
            if(domainMinistryDTO.getName() == null || domainMinistryDTO.getCountry() == null){
                log.error("MainServiceImpl.saveDomainMinistry.domainMinistryDTO is null");
                throw new MainServiceException("DomainMinistryDTO is null");
            }
            // Save domain ministry
            DomainMinistry domainMinistry = mainMapper.domainMinistryDTOToDomainMinistry(domainMinistryDTO);
            domainMinistry.setCreatedBy("ADMIN");
            domainMinistryRepo.save(domainMinistry);
            log.info("MainServiceImpl.saveDomainMinistry.domainMinistry saved: {}", domainMinistry.getName());
        }catch (Exception e){
            e.printStackTrace();
            throw new MainServiceException(e.getMessage());
        }
    }
}
