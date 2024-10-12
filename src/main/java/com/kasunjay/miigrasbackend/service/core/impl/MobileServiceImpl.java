package com.kasunjay.miigrasbackend.service.core.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.kasunjay.miigrasbackend.common.enums.Emotion;
import com.kasunjay.miigrasbackend.common.enums.Level;
import com.kasunjay.miigrasbackend.common.exception.MobileException;
import com.kasunjay.miigrasbackend.common.exception.UserException;
import com.kasunjay.miigrasbackend.common.util.AutherizedUserService;
import com.kasunjay.miigrasbackend.entity.mobile.EmployeeTracking;
import com.kasunjay.miigrasbackend.entity.mobile.Prediction;
import com.kasunjay.miigrasbackend.entity.mobile.SOS;
import com.kasunjay.miigrasbackend.entity.web.Employee;
import com.kasunjay.miigrasbackend.model.CommonSearchDTO;
import com.kasunjay.miigrasbackend.model.mobile.ChatContactDTO;
import com.kasunjay.miigrasbackend.model.mobile.LocationRequestDTO;
import com.kasunjay.miigrasbackend.model.mobile.PredictionModel;
import com.kasunjay.miigrasbackend.model.web.FirebaseNotificationDTO;
import com.kasunjay.miigrasbackend.repository.EmployeeRepo;
import com.kasunjay.miigrasbackend.repository.EmployeeTrackingRepo;
import com.kasunjay.miigrasbackend.repository.PredictionRepo;
import com.kasunjay.miigrasbackend.repository.SOSRepo;
import com.kasunjay.miigrasbackend.service.core.MobileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MobileServiceImpl implements MobileService {

    @Value("${application.emotion.prediction.url}")
    private String PREDICTION_MODEL_URL;

    private static final String EMPLOYEE_NOT_FOUND = "Employee not found";

    private final EmployeeRepo employeeRepo;

    private final PredictionRepo predictionRepo;

    private final EmployeeTrackingRepo employeeTrackingRepo;

    private final SOSRepo sosRepo;

    @Override
    public void predict(PredictionModel predictionModel) {
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
                Employee employee = employeeRepo.findById(predictionModel.getEmployeeId()).orElseThrow(() -> new UserException(EMPLOYEE_NOT_FOUND));
                employee.setFcmToken(predictionModel.getFcmToken());
                employee = employeeRepo.save(employee);
                prediction.setEmployee(employee);

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
                if(prediction.getEmotion().equals(Emotion.NEUTRAL) || prediction.getEmotion().equals(Emotion.HAPPY)){
                    prediction.setIsCheck(true);
                }
                prediction.setEmotionScore(highEmotionScores.get(Emotion.valueOf(highEmotionScores.keySet().toArray()[0].toString())));
                prediction.setCreatedBy(AutherizedUserService.getAutherizedUser().getUsername());
                predictionRepo.save(prediction);

                FirebaseNotificationDTO firebaseNotificationDTO = new FirebaseNotificationDTO();
                firebaseNotificationDTO.setFcmToken(predictionModel.getFcmToken());
                firebaseNotificationDTO.setTitle("SLBFE");
                firebaseNotificationDTO.setBody("Your message received us. Please be calm. We will contact you soon.");
                sendNotification(firebaseNotificationDTO);
            } else {
                log.warn("Request failed with status: " + response.getStatusCode());
                throw new MobileException("Request failed with status::: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Error during prediction", e);
            throw new MobileException(e.getMessage());
        }
    }

    @Override
    public void updateLocation(LocationRequestDTO locationRequestDTO) {
        try{
            log.info("MobileServiceImpl.updateLocation. employeeId: " + locationRequestDTO.getEmployeeId());
            Employee employee = employeeRepo.findById(locationRequestDTO.getEmployeeId()).orElseThrow(() -> new UserException(EMPLOYEE_NOT_FOUND));

            Optional<EmployeeTracking> employeeAndIsAvailable = employeeTrackingRepo.findByEmployeeAndIsAvailable(employee, true);
            if (employeeAndIsAvailable.isPresent()) {
                employeeAndIsAvailable.get().setIsAvailable(false);
                employeeTrackingRepo.save(employeeAndIsAvailable.get());
            }
            // update location
            EmployeeTracking employeeTracking = new EmployeeTracking();
            employeeTracking.setEmployee(employee);
            employeeTracking.setLatitude(locationRequestDTO.getLatitude());
            employeeTracking.setLongitude(locationRequestDTO.getLongitude());
            employeeTracking.setCreatedBy(AutherizedUserService.getAutherizedUser().getUsername());
            employeeTracking.setIsAvailable(true);
            employeeTrackingRepo.save(employeeTracking);
            // code to update location
            log.info("Location updated");

        }catch (Exception e){
            log.error("Error during location update", e);
            throw new MobileException(e.getMessage());
        }
    }

    @Override
    public void removeEveryLocationData() {
        try {
            log.info("MobileServiceImpl.removeEveryLocationData");
            Pageable pageable = PageRequest.of(0, 1); // Page 0, size 1
            List<EmployeeTracking> result = employeeTrackingRepo.findAllOrderByCreatedDateAsc(pageable, false);

            LocalDateTime createdDate = result.get(0).getCreatedDate();

            employeeTrackingRepo.deleteAllByCreatedDateBetween(createdDate.minusDays(1), createdDate.plusMonths(2));
            log.info("Location data removed");
        }catch (Exception e){
            log.error("Error during location data removal", e);
            throw new MobileException(e.getMessage());
        }
    }

    @Override
    public List<ChatContactDTO> findNearbyEmployees(CommonSearchDTO commonSearchDTO) {
        try {
            log.info("MobileServiceImpl.findNearbyEmployees. employeeId: " + commonSearchDTO.getRadius());
            Employee employee = employeeRepo.findById(commonSearchDTO.getId()).orElseThrow(() -> new UserException(EMPLOYEE_NOT_FOUND));
            Optional<EmployeeTracking> employeeTracking = employeeTrackingRepo.findByEmployeeAndIsAvailable(employee, true);
            if (employeeTracking.isEmpty()) {
                throw new MobileException("Location data not found");
            }
            if(commonSearchDTO.getRadius() == 0 || commonSearchDTO.getRadius() < 0){
                throw new MobileException("Invalid radius");
            }
            List<EmployeeTracking> nearbyEmployees = employeeTrackingRepo.findEmployeesWithinRadius(employeeTracking.get().getLatitude(),
                    employeeTracking.get().getLongitude(), commonSearchDTO.getRadius(), true);

            // Remove search employee from the nearbyEmployees list
            nearbyEmployees = nearbyEmployees.stream()
                    .filter(nearbyEmployee -> !nearbyEmployee.getEmployee().getId().equals(employee.getId()))
                    .collect(Collectors.toList());

            if (nearbyEmployees.isEmpty()) {
                return null;
            }

            List<ChatContactDTO> contactDTOList = new ArrayList<>();
            for (EmployeeTracking nearbyEmployee : nearbyEmployees) {
                ChatContactDTO chatContactDTO = new ChatContactDTO();
                chatContactDTO.setEmployeeId(nearbyEmployee.getEmployee().getId());
                chatContactDTO.setUserId(nearbyEmployee.getEmployee().getUser().getId());
                chatContactDTO.setName(nearbyEmployee.getEmployee().getPerson().getFirstName() + " " + nearbyEmployee.getEmployee().getPerson().getLastName());
                chatContactDTO.setEmail(nearbyEmployee.getEmployee().getPerson().getEmail());
                chatContactDTO.setPhone(nearbyEmployee.getEmployee().getPerson().getMobile1());
                chatContactDTO.setProfilePic(null);
                chatContactDTO.setJobType(nearbyEmployee.getEmployee().getJobType());
                chatContactDTO.setLatitude(nearbyEmployee.getLatitude());
                chatContactDTO.setLongitude(nearbyEmployee.getLongitude());
                contactDTOList.add(chatContactDTO);
            }
            return contactDTOList;
        }catch (Exception e){
            log.error("Error during finding nearby employees", e);
            throw new MobileException(e.getMessage());
        }
    }

    @Override
    public void sendNotification(FirebaseNotificationDTO firebaseNotificationDTO) {
        try {
            Notification notification = Notification.builder()
                    .setTitle(firebaseNotificationDTO.getTitle())
                    .setBody(firebaseNotificationDTO.getBody())
                    .build();

            Message message = Message.builder()
                    .setNotification(notification)
                    .setToken(firebaseNotificationDTO.getFcmToken()) // The recipient's FCM token
                    .build();
            // Send a message to the device corresponding to the provided registration token.
            FirebaseMessaging.getInstance().sendAsync(message)
                    .addListener(
                            () -> log.info("Successfully sent message: " + message),
                            e -> log.error("Error sending message: " + message, e)
                    );
        }catch (Exception e){
            log.error("Error during sending notification", e);
            throw new MobileException(e.getMessage());
        }

    }

    @Override
    public void sos(Long employeeId) {
        try {
            log.info("MobileServiceImpl.sos.employeeId: " + employeeId);
            Employee employee = employeeRepo.findById(employeeId).orElseThrow(() -> new UserException(EMPLOYEE_NOT_FOUND));
            sosRepo.findByEmployeeAndActive(employee, true).ifPresent(sos -> {
                sos.setActive(false);
                sosRepo.save(sos);
            });
            // save SOS
            SOS sos = new SOS();
            sos.setEmployee(employee);
            sos.setActive(true);
            sos.setCreatedBy(AutherizedUserService.getAutherizedUser().getUsername());
            sosRepo.save(sos);
            log.info("SOS sent");
        }catch (Exception e){
            log.error("Error during SOS", e);
            throw new MobileException(e.getMessage());
        }

    }

    private double calculateEmotionScore(JSONObject jsonObject){
        try {
            log.info("Calculating emotion score");
            JSONArray resultArray = jsonObject.getJSONArray("result");

            double angerScore = 0,
                    disgustScore = 0,
                    fearScore = 0,
                    sadnessScore = 0;
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
                    default:
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
                default:
                    highEmotionScores.put(Emotion.NEUTRAL, previousScore);
            }
            return highEmotionScores;
        }catch (Exception e){
            log.error("Error during prediction", e);
            throw new MobileException(e.getMessage());
        }
    }
}
