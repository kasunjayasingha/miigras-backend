package com.kasunjay.miigrasbackend.controller.mobile;

import com.kasunjay.miigrasbackend.common.enums.Success;
import com.kasunjay.miigrasbackend.model.StandardResponse;
import com.kasunjay.miigrasbackend.model.mobile.PredictionModel;
import com.kasunjay.miigrasbackend.service.core.MobileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mobile")
@Slf4j
@RequiredArgsConstructor
public class MobileController {

    private final MobileService mobileService;

    @PostMapping("/predict")
    @PreAuthorize("hasAuthority('worker:create')")
    public ResponseEntity<StandardResponse> predict(@RequestBody PredictionModel predictionModel, @RequestHeader("Access-Token") String token) {
        log.info("MobileController.predict. employeeId: " + predictionModel.getEmployeeId());
        mobileService.predict(predictionModel);
        return new ResponseEntity<>(new StandardResponse(HttpStatus.OK, Success.SUCCESS, "Prediction done"), HttpStatus.OK);
    }


}
