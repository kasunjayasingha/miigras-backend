package com.kasunjay.miigrasbackend.service.core;

import com.kasunjay.miigrasbackend.model.mobile.LocationRequestDTO;
import com.kasunjay.miigrasbackend.model.mobile.PredictionModel;

public interface MobileService {
    void predict(PredictionModel predictionModel);

    void updateLocation(LocationRequestDTO locationRequestDTO);

    void removeEveryLocationData();
}
