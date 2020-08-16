package com.hireach.congestiontracingstandalone.service;

import com.hireach.congestiontracingstandalone.entity.Company;
import com.hireach.congestiontracingstandalone.entity.DeviceLocationHistory;
import com.hireach.congestiontracingstandalone.repository.DeviceLocationHistoryRepository;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.List;

import static com.hireach.congestiontracingstandalone.util.Util.createPoint;

@Service
public class DeviceLocationHistoryService {

    @Value("${mlservice.url}")
    private String ML_SERVICE_URL;

    private final WebClient webClient;
    private final DeviceLocationHistoryRepository deviceLocationHistoryRepository;

    public DeviceLocationHistoryService(final WebClient.Builder webClient,
                                        final DeviceLocationHistoryRepository deviceLocationHistoryRepository) {
        this.webClient = webClient.build();
        this.deviceLocationHistoryRepository = deviceLocationHistoryRepository;
    }

    public void saveDeviceLocationHistory(double lat, double lon, String deviceId, Company company, Instant instant) {
        Point point = createPoint(lat, lon);

        deviceLocationHistoryRepository.save(DeviceLocationHistory.builder()
                .locationPoint(point)
                .deviceId(deviceId)
                .updatedAt(instant)
                .company(company)
                .build());
    }

    public List<DeviceLocationHistoryRepository.MLDataModel> getHistory(double lat, double lon, double radius) {
        Point point = createPoint(lat, lon);

        return deviceLocationHistoryRepository.getHistory(point, radius);
    }

    public int getPrediction(double lat, double lon, double radius) {
        Point point = createPoint(lat, lon);
        List<DeviceLocationHistoryRepository.MLDataModel> deviceLocationHistory = deviceLocationHistoryRepository.getHistory(point, 10D);

        return webClient
                .post()
                .uri(ML_SERVICE_URL + "/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(deviceLocationHistory))
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
    }

}
