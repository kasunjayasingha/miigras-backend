package com.kasunjay.miigrasbackend.service.core.impl;

import com.kasunjay.miigrasbackend.common.enums.Success;
import com.kasunjay.miigrasbackend.common.exception.MainServiceException;
import com.kasunjay.miigrasbackend.common.mapper.MainMapper;
import com.kasunjay.miigrasbackend.common.util.AutherizedUserService;
import com.kasunjay.miigrasbackend.entity.User;
import com.kasunjay.miigrasbackend.entity.web.*;
import com.kasunjay.miigrasbackend.model.StandardResponse;
import com.kasunjay.miigrasbackend.model.web.*;
import com.kasunjay.miigrasbackend.repository.*;
import com.kasunjay.miigrasbackend.service.core.MainService;
import com.kasunjay.miigrasbackend.service.core.UserService;
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
    private final GradientRepo gradientRepo;
    private final EmployeeRepo employeeRepo;
    private final PersonRepo personRepo;
    private final UserService userService;

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

    @Override
    public void saveEmployee(EmployeeDTO employeeDTO) {
        log.info("MainServiceImpl.saveEmployee.called");
        try {
            if(employeeDTO.getPerson() == null || employeeDTO.getUser() == null ||
                    employeeDTO.getAgency() == null || employeeDTO.getGradient() == null){
                log.error("MainServiceImpl.saveEmployee.employeeDTO is null");
                throw new MainServiceException("EmployeeDTO is null");
            }
            Gradient gradient = saveGradient(employeeDTO.getGradient());
            Person person = null;
            if(employeeDTO.getGradient().getSameAsEmployeeAddress()){
                person = savePersonIfSameAddress(employeeDTO.getPerson(), gradient.getPerson().getAddress());
            }else {
                person = savePerson(employeeDTO.getPerson());
            }
            Employee employee = mainMapper.employeeDTOToEmployee(employeeDTO);
            employee.setPerson(person);
            employee.setGradient(gradient);
            User user = userService.registerUser(employeeDTO.getUser());
            user.setEnabled(true);
            employee.setUser(user);
            employee.setCreatedBy(AutherizedUserService.getAutherizedUser().getUsername());
            employeeRepo.save(employee);
        }catch (Exception e){
            e.printStackTrace();
            throw new MainServiceException(e.getMessage());
        }
    }

    @Override
    public List<EmployeeDTO> getEmployeeList() {
        log.info("MainServiceImpl.getEmployeeList.called");
        try {
            return mainMapper.mapToEmployeeDTOList(employeeRepo.findAll());
        }catch (Exception e){
            e.printStackTrace();
            throw new MainServiceException(e.getMessage());
        }
    }

    @Override
    public StandardResponse generateEmployeeId() {
        return new StandardResponse(HttpStatus.OK, Success.SUCCESS, "EMP" + System.currentTimeMillis());
    }

    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        log.info("MainServiceImpl.updateEmployee.called");
        try {
            if(employeeDTO.getId() ==0){
                log.error("MainServiceImpl.updateEmployee.employeeDTO is null");
                throw new MainServiceException("EmployeeDTO is null");
            }
            Employee employee = employeeRepo.findById(employeeDTO.getId()).orElse(null);
            if(employee == null){
                log.error("MainServiceImpl.updateEmployee.employee is null");
                throw new MainServiceException("Employee is null");
            }
            Gradient gradient = saveGradient(employeeDTO.getGradient());
            Person person = null;
            if(employeeDTO.getGradient().getSameAsEmployeeAddress()){
                person = savePersonIfSameAddress(employeeDTO.getPerson(), gradient.getPerson().getAddress());
            }else {
                person = savePerson(employeeDTO.getPerson());
            }
            employee.setPerson(person);
            employee.setGradient(gradient);
            employee.setUpdatedBy(AutherizedUserService.getAutherizedUser().getUsername());
            employeeRepo.save(employee);
        }catch (Exception e){
            e.printStackTrace();
            throw new MainServiceException(e.getMessage());
        }
    }

    private Gradient saveGradient(GradientDTO gradientDTO){
        if (gradientDTO == null || gradientDTO.getPerson() == null){
            log.error("MainServiceImpl.saveGradient.gradientDTO is null");
            throw new MainServiceException("GradientDTO is null");
        }
        Gradient gradient = mainMapper.gradientDTOToGradient(gradientDTO);
        gradient.setPerson(savePerson(gradientDTO.getPerson()));
        gradient.setCreatedBy(AutherizedUserService.getAutherizedUser().getUsername());
        return gradientRepo.save(gradient);
    }

    private Person savePersonIfSameAddress(PersonDTO personDTO, Address address){
        if (personDTO == null){
            log.error("MainServiceImpl.savePersonIfSameAddress.personDTO is null");
            throw new MainServiceException("PersonDTO is null");
        }
        Person person = mainMapper.personDTOToPerson(personDTO);
        person.setAddress(address);
        person.setCreatedBy(AutherizedUserService.getAutherizedUser().getUsername());
        return personRepo.save(person);
    }

    private Person savePerson(PersonDTO personDTO){
        if (personDTO == null){
            log.error("MainServiceImpl.savePerson.personDTO is null");
            throw new MainServiceException("PersonDTO is null");
        }
        Person person = mainMapper.personDTOToPerson(personDTO);
        person.setAddress(saveAddress(personDTO.getAddress()));
        person.setCreatedBy(AutherizedUserService.getAutherizedUser().getUsername());
        return personRepo.save(person);
    }

    private Address saveAddress(AddressDTO addressDTO){
        if (addressDTO == null){
            log.error("MainServiceImpl.saveAddress.addressDTO is null");
            throw new MainServiceException("AddressDTO is null");
        }
        return addressRepo.save(mainMapper.addressDTOToAddress(addressDTO));
    }
}
