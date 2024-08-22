package com.kasunjay.miigrasbackend.common.mapper;

import com.kasunjay.miigrasbackend.entity.mobile.EmployeeTracking;
import com.kasunjay.miigrasbackend.entity.mobile.Prediction;
import com.kasunjay.miigrasbackend.entity.web.Employee;
import com.kasunjay.miigrasbackend.model.mobile.EmployeeTrackingDTO;
import com.kasunjay.miigrasbackend.model.mobile.PredictionDTO;
import com.kasunjay.miigrasbackend.model.web.EmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DashboardMapper {

    @Mapping(target = "employee", ignore = true)
    EmployeeTrackingDTO employeeTrackingToEmployeeTrackingDTO(EmployeeTracking employeeTracking);

    @Mapping(target = "employee", ignore = true)
    PredictionDTO predictionToPredictionDTO(Prediction prediction);
}
