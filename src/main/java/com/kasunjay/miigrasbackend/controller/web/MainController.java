package com.kasunjay.miigrasbackend.controller.web;

import com.kasunjay.miigrasbackend.common.enums.Success;
import com.kasunjay.miigrasbackend.entity.web.Country;
import com.kasunjay.miigrasbackend.model.web.CountryDTO;
import com.kasunjay.miigrasbackend.model.web.DomainMinistryDTO;
import com.kasunjay.miigrasbackend.model.StandardResponse;
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

    @PostMapping("/saveDomainMinistry")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<StandardResponse> saveDomainMinistry(@RequestBody DomainMinistryDTO domainMinistryDTO) {
        log.info("MainController.saveDomainMinistry.called");
        mainService.saveDomainMinistry(domainMinistryDTO);
        return new ResponseEntity<>(new StandardResponse(HttpStatus.OK, Success.SUCCESS, "Domain Ministry saved"), HttpStatus.OK);
    }
}
