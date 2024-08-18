package com.kasunjay.miigrasbackend.controller.mobile;

import com.kasunjay.miigrasbackend.common.enums.Success;
import com.kasunjay.miigrasbackend.model.CommonSearchDTO;
import com.kasunjay.miigrasbackend.model.StandardResponse;
import com.kasunjay.miigrasbackend.model.mobile.ChatContactDTO;
import com.kasunjay.miigrasbackend.model.mobile.LocationRequestDTO;
import com.kasunjay.miigrasbackend.model.mobile.PredictionModel;
import com.kasunjay.miigrasbackend.model.web.EmployeeDTO;
import com.kasunjay.miigrasbackend.service.core.MainService;
import com.kasunjay.miigrasbackend.service.core.MobileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mobile")
@Slf4j
@RequiredArgsConstructor
public class MobileController {

    private final MobileService mobileService;
    private final MainService mainService;

    @PostMapping("/predict")
    @PreAuthorize("hasAuthority('worker:create')")
    public ResponseEntity<StandardResponse> predict(@RequestBody PredictionModel predictionModel, @RequestHeader("Access-Token") String token) {
        log.info("MobileController.predict. employeeId: " + predictionModel.getEmployeeId());
        mobileService.predict(predictionModel);
        return new ResponseEntity<>(new StandardResponse(HttpStatus.OK, Success.SUCCESS, "Prediction done"), HttpStatus.OK);
    }

    @GetMapping("/getEmployeeByUserId")
    @PreAuthorize("hasAuthority('worker:read')")
    public ResponseEntity<EmployeeDTO> getEmployeeByUserId(@RequestParam Long userId) {
        log.info("MobileController.getEmployeeByUserId. userId: " + userId);
        return new ResponseEntity<>(mainService.getEmployeeByUserId(userId), HttpStatus.OK);
    }

    @PostMapping("/updateLocation")
    @PreAuthorize("hasAuthority('worker:update')")
    public ResponseEntity<StandardResponse> updateLocation(@RequestBody LocationRequestDTO locationRequestDTO) {
        log.info("MobileController.updateLocation. employeeId: " + locationRequestDTO.getEmployeeId());
        mobileService.updateLocation(locationRequestDTO);
        return new ResponseEntity<>(new StandardResponse(HttpStatus.OK, Success.SUCCESS, "Location updated"), HttpStatus.OK);
    }

    @GetMapping("/find-nearby-employees")
    @PreAuthorize("hasAuthority('worker:read')")
    public ResponseEntity<List<ChatContactDTO>> findNearbyEmployees(@RequestBody CommonSearchDTO commonSearchDTO) {
        log.info("MobileController.findNearbyEmployees. employeeId: " + commonSearchDTO.getId());
        return new ResponseEntity<>(mobileService.findNearbyEmployees(commonSearchDTO), HttpStatus.OK);
    }


}
