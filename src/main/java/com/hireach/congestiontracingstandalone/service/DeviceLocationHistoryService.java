package com.hireach.congestiontracingstandalone.service;

import com.hireach.congestiontracingstandalone.dao.DeviceLocationHistoryDao;
import com.hireach.congestiontracingstandalone.entity.Company;
import com.hireach.congestiontracingstandalone.entity.DeviceLocationHistory;
import com.hireach.congestiontracingstandalone.model.MLDataModel;
import com.hireach.congestiontracingstandalone.repository.DeviceLocationHistoryRepository;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(DeviceLocationHistoryService.class);

    @Value("${mlservice.url}")
    private String ML_SERVICE_URL;

    private final WebClient webClient;
    private final DeviceLocationHistoryRepository deviceLocationHistoryRepository;
    private final DeviceLocationHistoryDao deviceLocationHistoryDao;

    public DeviceLocationHistoryService(final WebClient.Builder webClient,
                                        final DeviceLocationHistoryRepository deviceLocationHistoryRepository,
                                        final DeviceLocationHistoryDao deviceLocationHistoryDao) {
        this.webClient = webClient.build();
        this.deviceLocationHistoryRepository = deviceLocationHistoryRepository;
        this.deviceLocationHistoryDao = deviceLocationHistoryDao;
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

    public List<MLDataModel> getHistory(double lat, double lon, double radius) {
        Point point = createPoint(lat, lon);

        return deviceLocationHistoryDao.getHistory(point, radius);
    }

    public String getPrediction(double lat, double lon, double radius, Instant predictionDate) {
        Point point = createPoint(lat, lon);
        LOG.info("Getting history for point lon " + lon + ", lat " + lat);
        List<MLDataModel> deviceLocationHistory = deviceLocationHistoryDao.getHistory(point, 10D);
        LOG.info("Sending request to the prediction service...");

        return webClient
                .post()
                .uri(ML_SERVICE_URL + "/predict?prediction_date=" + predictionDate)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(deviceLocationHistory))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
