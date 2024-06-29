package com.kasunjay.miigrasbackend.service.core.impl;

import com.kasunjay.miigrasbackend.common.enums.Success;
import com.kasunjay.miigrasbackend.common.exception.MainServiceException;
import com.kasunjay.miigrasbackend.common.mapper.MainMapper;
import com.kasunjay.miigrasbackend.common.util.AutherizedUserService;
import com.kasunjay.miigrasbackend.entity.User;
import com.kasunjay.miigrasbackend.entity.web.Address;
import com.kasunjay.miigrasbackend.entity.web.Agency;
import com.kasunjay.miigrasbackend.entity.web.Country;
import com.kasunjay.miigrasbackend.entity.web.DomainMinistry;
import com.kasunjay.miigrasbackend.model.StandardResponse;
import com.kasunjay.miigrasbackend.model.web.AddressDTO;
import com.kasunjay.miigrasbackend.model.web.AgencyDTO;
import com.kasunjay.miigrasbackend.model.web.CountryDTO;
import com.kasunjay.miigrasbackend.model.web.DomainMinistryDTO;
import com.kasunjay.miigrasbackend.repository.AddressRepo;
import com.kasunjay.miigrasbackend.repository.AgencyRepo;
import com.kasunjay.miigrasbackend.repository.CountryRepo;
import com.kasunjay.miigrasbackend.repository.DomainMinistryRepo;
import com.kasunjay.miigrasbackend.service.core.MainService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AddressRepo addressRepo;
    private final AgencyRepo agencyRepo;

    @Override
    public void saveCountry(CountryDTO countryDTO) {
        try {
            if(countryDTO.getCode() == null || countryDTO.getName() == null || countryDTO.getNtpTime() == null){
                log.error("MainServiceImpl.saveCountry.country is null");
                throw new MainServiceException("Country is null");
            }
            // Save country
            Country country = mainMapper.countryDTOToCountry(countryDTO);
            country.setCreatedBy(AutherizedUserService.getAutherizedUser().getUsername());
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
            return countryRepo.findAll();
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
            domainMinistry.setCreatedBy(AutherizedUserService.getAutherizedUser().getUsername());
            domainMinistryRepo.save(domainMinistry);
            log.info("MainServiceImpl.saveDomainMinistry.domainMinistry saved: {}", domainMinistry.getName());
        }catch (Exception e){
            e.printStackTrace();
            throw new MainServiceException(e.getMessage());
        }
    }

    @Override
    public StandardResponse checkCountryIsPresent(String name, String code) {
        if (countryRepo.existsByName(name) || countryRepo.existsByCode(code)){
            return new StandardResponse(HttpStatus.OK, Success.SUCCESS, "Country found");
        }
        return new StandardResponse(HttpStatus.NOT_FOUND, Success.FAILURE, "Country not found");
    }

    @Override
    public void deleteCountry(Long id) {
        try {
            countryRepo.deleteById(id);
            log.info("MainServiceImpl.deleteCountry.country deleted: {}", id);
        }catch (Exception e) {
            e.printStackTrace();
            throw new MainServiceException(e.getMessage());
        }
    }

    @Override
    public List<DomainMinistryDTO> getDomainMinistryList() {
        try {
            return mainMapper.mapToDomainMinistryDTOList(domainMinistryRepo.findAll());
        }catch (Exception e){
            e.printStackTrace();
            throw new MainServiceException(e.getMessage());
        }
    }

    @Override
    public void saveAgency(AgencyDTO agencyDTO) {
        log.info("MainServiceImpl.saveAgency.called");
        try{
            if(agencyDTO.getDomainMinistry().getId() == null){
                log.error("MainServiceImpl.saveAgency.domainMinistry is null");
                throw new MainServiceException("DomainMinistry is null");
            }
            Agency agency = mainMapper.agencyDTOToAgency(agencyDTO);
            agency.setAddressAgency(saveAddress(agencyDTO.getAddressAgency()));
            agency.setCreatedBy(AutherizedUserService.getAutherizedUser().getUsername());
            agency.setStatus(true);
            agencyRepo.save(agency);
            log.info("MainServiceImpl.saveAgency.agency saved: {}", agency.getName());
        }catch (Exception e){
            e.printStackTrace();
            throw new MainServiceException(e.getMessage());
        }
    }

    @Override
    public List<AgencyDTO> getAgencyList() {
        log.info("MainServiceImpl.getAgencyList.called");
        try {
            return mainMapper.mapToAgencyDTOList(agencyRepo.findAllByStatus(true));
        }catch (Exception e){
            e.printStackTrace();
            throw new MainServiceException(e.getMessage());
        }
    }

    private Address saveAddress(AddressDTO addressDTO){
        if (addressDTO == null){
            log.error("MainServiceImpl.saveAddress.addressDTO is null");
            throw new MainServiceException("AddressDTO is null");
        }
        return addressRepo.save(mainMapper.addressDTOToAddress(addressDTO));
    }
}
