package com.kasunjay.miigrasbackend.controller.web;

import com.kasunjay.miigrasbackend.common.enums.Success;
import com.kasunjay.miigrasbackend.entity.web.Country;
import com.kasunjay.miigrasbackend.model.web.AgencyDTO;
import com.kasunjay.miigrasbackend.model.web.CountryDTO;
import com.kasunjay.miigrasbackend.model.web.DomainMinistryDTO;
import com.kasunjay.miigrasbackend.model.StandardResponse;
import com.kasunjay.miigrasbackend.model.web.EmployeeDTO;
import com.kasunjay.miigrasbackend.service.core.MainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/main")
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @PostMapping("/saveCountry")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<StandardResponse> saveCountry(@RequestBody @Valid CountryDTO country) {
        System.out.println("Authorities: "+ SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
        log.info("MainController.saveCountry. country");
        mainService.saveCountry(country);
        return new ResponseEntity<>(new StandardResponse(HttpStatus.OK, Success.SUCCESS, "Country saved"), HttpStatus.OK);
    }

    @GetMapping("/getCountryList")
    public ResponseEntity<List<Country>> getCountryList() {
        log.info("MainController.getCountry.code:");
        return new ResponseEntity<>(mainService.getCountryList(), HttpStatus.OK);
    }

    @GetMapping("/checkCountryIsPresent")
    public ResponseEntity<StandardResponse> getCountryByCode(@RequestParam String code, @RequestParam String name) {
        log.info("MainController.getCountryByCode.code:" + code);
        return new ResponseEntity<>(mainService.checkCountryIsPresent(name, code), HttpStatus.OK);
    }

    @DeleteMapping("/deleteCountry")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<StandardResponse> deleteCountry(@RequestParam Long id) {
        log.info("MainController.deleteCountry.id:" + id);
        mainService.deleteCountry(id);
        return new ResponseEntity<>(new StandardResponse(HttpStatus.OK, Success.SUCCESS, "Country deleted"), HttpStatus.OK);
    }

    @PostMapping("/saveDomainMinistry")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<StandardResponse> saveDomainMinistry(@RequestBody DomainMinistryDTO domainMinistryDTO) {
        log.info("MainController.saveDomainMinistry.called");
        mainService.saveDomainMinistry(domainMinistryDTO);
        return new ResponseEntity<>(new StandardResponse(HttpStatus.OK, Success.SUCCESS, "Domain Ministry saved"), HttpStatus.OK);
    }

    @GetMapping("/getDomainMinistryList")
    public ResponseEntity<List<DomainMinistryDTO>> getDomainMinistryList() {
        log.info("MainController.getDomainMinistryList.called");
        return new ResponseEntity<>(mainService.getDomainMinistryList(), HttpStatus.OK);
    }

    @PostMapping("/saveAgency")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<StandardResponse> saveAgency(@RequestBody AgencyDTO agencyDTO) {
        log.info("MainController.saveAgency.called");
        mainService.saveAgency(agencyDTO);
        return new ResponseEntity<>(new StandardResponse(HttpStatus.OK, Success.SUCCESS, "Agency saved"), HttpStatus.OK);
    }

    @GetMapping("/getAgencyList")
    public ResponseEntity<List<AgencyDTO>> getAgencyList() {
        log.info("MainController.getAgencyList.called");
        return new ResponseEntity<>(mainService.getAgencyList(), HttpStatus.OK);
    }

    @PostMapping("/saveEmployee")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<StandardResponse> saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("MainController.saveEmployee.called");
        mainService.saveEmployee(employeeDTO);
        return new ResponseEntity<>(new StandardResponse(HttpStatus.OK, Success.SUCCESS, "Employee saved"), HttpStatus.OK);
    }

    @GetMapping("/getEmployeeList")
    public ResponseEntity<List<EmployeeDTO>> getEmployeeList() {
        log.info("MainController.getEmployeeList.called");
        return new ResponseEntity<>(mainService.getEmployeeList(), HttpStatus.OK);
    }

    @GetMapping("/generateEmployeId")
    public ResponseEntity<String> generateEmployeId() {
        log.info("MainController.generateEmployeId.called");
        return new ResponseEntity<>(mainService.generateEmployeId(), HttpStatus.OK);
    }
}
