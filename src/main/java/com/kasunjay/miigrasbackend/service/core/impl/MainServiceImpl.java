package com.kasunjay.miigrasbackend.service.core.impl;

import com.kasunjay.miigrasbackend.common.exception.MainServiceException;
import com.kasunjay.miigrasbackend.entity.web.Country;
import com.kasunjay.miigrasbackend.repository.CountryRepo;
import com.kasunjay.miigrasbackend.service.core.MainService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final CountryRepo countryRepo;

    @Override
    public void saveCountry(Country country) {
        try {
            if(country.getCode() == null || country.getName() == null || country.getNtpTime() == null){
                log.error("MainServiceImpl.saveCountry.country is null");
                throw new MainServiceException("Country is null");
            }
            // Save country
            countryRepo.save(country);
            log.info("MainServiceImpl.saveCountry.country saved: {}", country.getName());
        }catch (Exception e){
            e.printStackTrace();
            throw new MainServiceException(e.getMessage());
        }
    }
}
