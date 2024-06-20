package com.kasunjay.miigrasbackend.controller.web;

import com.kasunjay.miigrasbackend.common.enums.Success;
import com.kasunjay.miigrasbackend.entity.web.Country;
import com.kasunjay.miigrasbackend.model.DomainMinistryDTO;
import com.kasunjay.miigrasbackend.model.StandardResponse;
import com.kasunjay.miigrasbackend.service.core.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/main")
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @PostMapping("/saveCountry")
    public ResponseEntity<StandardResponse> saveCountry(@RequestBody Country country) {
        log.info("MainController.saveCountry. country");
        mainService.saveCountry(country);
        return new ResponseEntity<>(new StandardResponse(HttpStatus.OK, Success.SUCCESS, "Country saved"), HttpStatus.OK);
    }

    @PostMapping("/saveDomainMinistry")
    public ResponseEntity<StandardResponse> saveDomainMinistry(@RequestBody DomainMinistryDTO domainMinistryDTO) {
        log.info("MainController.saveDomainMinistry.called");
        mainService.saveDomainMinistry(domainMinistryDTO);
        return new ResponseEntity<>(new StandardResponse(HttpStatus.OK, Success.SUCCESS, "Domain Ministry saved"), HttpStatus.OK);
    }
}
