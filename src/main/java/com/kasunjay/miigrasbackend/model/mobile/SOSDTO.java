package com.kasunjay.miigrasbackend.model.mobile;

import com.kasunjay.miigrasbackend.model.web.EmployeeDTO;
import lombok.Data;

@Data
public class SOSDTO {
    private Boolean active;
    private EmployeeDTO employee;
}
