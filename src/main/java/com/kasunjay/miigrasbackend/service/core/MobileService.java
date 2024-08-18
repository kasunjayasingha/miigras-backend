package com.kasunjay.miigrasbackend.service.core;

import com.kasunjay.miigrasbackend.model.CommonSearchDTO;
import com.kasunjay.miigrasbackend.model.mobile.ChatContactDTO;
import com.kasunjay.miigrasbackend.model.mobile.LocationRequestDTO;
import com.kasunjay.miigrasbackend.model.mobile.PredictionModel;

import java.util.List;

public interface MobileService {
    void predict(PredictionModel predictionModel);

    void updateLocation(LocationRequestDTO locationRequestDTO);

    void removeEveryLocationData();

    List<ChatContactDTO> findNearbyEmployees(CommonSearchDTO commonSearchDTO);
}
