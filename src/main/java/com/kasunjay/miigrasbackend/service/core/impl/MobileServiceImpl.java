package com.kasunjay.miigrasbackend.service.core.impl;

import com.kasunjay.miigrasbackend.common.exception.MobileException;
import com.kasunjay.miigrasbackend.common.exception.UserException;
import com.kasunjay.miigrasbackend.model.mobile.PredictionModel;
import com.kasunjay.miigrasbackend.service.core.MobileService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@Slf4j
public class MobileServiceImpl implements MobileService {

    @Value("${application.emotion.prediction.url}")
    private String PREDICTION_MODEL_URL;

    @Override
    public void predict(PredictionModel predictionModel) {
        log.info("MobileServiceImpl.predict. employeeId: " + predictionModel.getEmployeeId());
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PredictionModel> reqEntity = new HttpEntity<>(predictionModel, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(PREDICTION_MODEL_URL, reqEntity, String.class);
            log.info("Sending request to URL: " + PREDICTION_MODEL_URL);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Prediction done");

                JSONObject jsonObject = new JSONObject( response.getBody());
                JSONArray resultArray = jsonObject.getJSONArray("result");

                double angerScore = 0, disgustScore = 0, fearScore = 0, sadnessScore = 0;
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject resultObject = resultArray.getJSONObject(i);
                    String label = resultObject.getString("label");
                    double score = resultObject.getDouble("score");
                    switch (label) {
                        case "anger":
                            angerScore = score;
                            break;
                        case "disgust":
                            disgustScore = score;
                            break;
                        case "fear":
                            fearScore = score;
                            break;
                        case "sadness":
                            sadnessScore = score;
                            break;
                    }
                }

                double averageScore = (angerScore + disgustScore + fearScore + sadnessScore) / 4;
                double percentage = (averageScore / 10) * 100; // assuming scores range from -10 to +10

                log.info("Average Score: " + averageScore);
                log.info("Percentage: " + percentage + "%");

            } else {
                log.warn("Request failed with status: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Error during prediction", e);
            throw new MobileException(e.getMessage());
        }
    }
}
