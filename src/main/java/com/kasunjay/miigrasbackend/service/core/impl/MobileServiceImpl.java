package com.kasunjay.miigrasbackend.service.core.impl;

import com.kasunjay.miigrasbackend.common.enums.Emotion;
import com.kasunjay.miigrasbackend.common.enums.Level;
import com.kasunjay.miigrasbackend.common.exception.MobileException;
import com.kasunjay.miigrasbackend.common.exception.UserException;
import com.kasunjay.miigrasbackend.common.util.AutherizedUserService;
import com.kasunjay.miigrasbackend.entity.mobile.Prediction;
import com.kasunjay.miigrasbackend.model.mobile.PredictionModel;
import com.kasunjay.miigrasbackend.repository.EmployeeRepo;
import com.kasunjay.miigrasbackend.repository.PredictionRepo;
import com.kasunjay.miigrasbackend.service.core.MobileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

import java.util.HashMap;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MobileServiceImpl implements MobileService {

    @Value("${application.emotion.prediction.url}")
    private String PREDICTION_MODEL_URL;

    private final EmployeeRepo employeeRepo;

    private final PredictionRepo predictionRepo;

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
                Prediction prediction = new Prediction();
                prediction.setEmployee(employeeRepo.findById(predictionModel.getEmployeeId()).orElseThrow(() -> new UserException("Employee not found")));
                prediction.setMessage(predictionModel.getMessage());
                prediction.setScore(calculateEmotionScore(new JSONObject(response.getBody())));

                if(prediction.getScore() < 5){
                    prediction.setSeverity(Level.LOW);
                }else if(prediction.getScore() >= 5 && prediction.getScore() < 9){
                    prediction.setSeverity(Level.MEDIUM);
                }else{
                    prediction.setSeverity(Level.HIGH);
                }

                HashMap<Emotion, Double> highEmotionScores = getHighEmotionScores(new JSONObject(response.getBody()));

                prediction.setEmotion((Emotion) highEmotionScores.keySet().toArray()[0]);
                prediction.setEmotionScore(highEmotionScores.get(Emotion.valueOf(highEmotionScores.keySet().toArray()[0].toString())));
                prediction.setCreatedBy(AutherizedUserService.getAutherizedUser().getUsername());
                predictionRepo.save(prediction);
            } else {
                log.warn("Request failed with status: " + response.getStatusCode());
                throw new MobileException("Request failed with status::: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Error during prediction", e);
            throw new MobileException(e.getMessage());
        }
    }

    private double calculateEmotionScore(JSONObject jsonObject){
        try {
            log.info("Calculating emotion score");
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
            return percentage;

        }catch (Exception e){
            log.error("Error during prediction", e);
            throw new MobileException(e.getMessage());
        }
    }

    private HashMap<Emotion, Double> getHighEmotionScores(JSONObject jsonObject) {
        try {
            HashMap<Emotion, Double> highEmotionScores = new HashMap<>();
            JSONArray resultArray = jsonObject.getJSONArray("result");
            String previousLabel = "";
            double previousScore = 0;

            String currentLabel = "";
            double currentScore = 0;
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject resultObject = resultArray.getJSONObject(i);
                currentLabel = resultObject.getString("label");
                currentScore = resultObject.getDouble("score");
                if (currentScore > previousScore) {
                    previousLabel = currentLabel;
                    previousScore = currentScore;
                }
            }

            switch (previousLabel) {
                case "fear", "anger", "disgust":
                    highEmotionScores.put(Emotion.FEAR, previousScore);
                    break;
                case "sadness":
                    highEmotionScores.put(Emotion.SAD, previousScore);
                    break;
                case "joy", "surprise":
                    highEmotionScores.put(Emotion.HAPPY, previousScore);
                    break;
                case "neutral":
                    highEmotionScores.put(Emotion.NEUTRAL, previousScore);
                    break;
            }
            return highEmotionScores;
        }catch (Exception e){
            log.error("Error during prediction", e);
            throw new MobileException(e.getMessage());
        }
    }
}
