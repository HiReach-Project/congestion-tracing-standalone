//    Congestion API - a REST API built to track congestion spots and
//    crowded areas using real-time location data from mobile devices.
//
//    Copyright (C) 2020, University Politehnica of Bucharest, member
//    of the HiReach Project consortium <https://hireach-project.eu/>
//    <andrei[dot]gheorghiu[at]upb[dot]ro. This project has received
//    funding from the European Union’s Horizon 2020 research and
//    innovation programme under grant agreement no. 769819.
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <https://www.gnu.org/licenses/>.
package com.hireach.congestiontracingstandalone.service;

import com.hireach.congestiontracingstandalone.dao.DeviceLocationHistoryDao;
import com.hireach.congestiontracingstandalone.entity.Company;
import com.hireach.congestiontracingstandalone.entity.DeviceLocationHistory;
import com.hireach.congestiontracingstandalone.model.MLDataModel;
import com.hireach.congestiontracingstandalone.repository.DeviceLocationHistoryRepository;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.time.Instant;
import java.util.List;

import static com.hireach.congestiontracingstandalone.util.Util.createPoint;

@Service
public class DeviceLocationHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceLocationHistoryService.class);

    @Value("${mlservice.url}")
    private String ML_SERVICE_URL;

    @Value("${mlservice.access.key}")
    private String ML_ACCESS_KEY;

    private final WebClient webClient;
    private final DeviceLocationHistoryRepository deviceLocationHistoryRepository;
    private final DeviceLocationHistoryDao deviceLocationHistoryDao;

    public DeviceLocationHistoryService(final WebClient.Builder webClient,
                                        final DeviceLocationHistoryRepository deviceLocationHistoryRepository,
                                        final DeviceLocationHistoryDao deviceLocationHistoryDao) {
        this.webClient = webClient.clientConnector(new ReactorClientHttpConnector(HttpClient.from(
                TcpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                        .doOnConnected(connection ->
                                connection.addHandlerLast(new ReadTimeoutHandler(10))
                                        .addHandlerLast(new WriteTimeoutHandler(10))))
        )).build();
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

        String response = sendPredictRequest(lat, lon, radius, predictionDate);
        if (response.equalsIgnoreCase("not cached")) {
            LOG.debug("Getting history for point lon " + lon + ", lat " + lat);
            List<MLDataModel> deviceLocationHistory = deviceLocationHistoryDao.getHistory(point, radius);
            response = sendPredictRequest(lat, lon, radius, predictionDate, deviceLocationHistory);
        }
        return response;

    }

    private String sendPredictRequest(double lat, double lon, double radius, Instant predictionDate) {
        String predictUrl = buildRequestUrl(lat, lon, radius, predictionDate);

        LOG.debug("Sending request to the prediction service...");
        try {
            return webClient
                    .post()
                    .uri(predictUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            LOG.error("Something went wrong on the prediction service!");
            throw e;
        } catch (Exception e) {
            LOG.error("Failed to connect to the prediction service! Make sure the ML service is running and can be accessed via network calls.");
            throw e;
        }
    }

    private String sendPredictRequest(double lat, double lon, double radius, Instant predictionDate,
                                      List<MLDataModel> deviceLocationHistory) {
        String predictUrl = buildRequestUrl(lat, lon, radius, predictionDate);

        try {
            return webClient
                    .post()
                    .uri(predictUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(deviceLocationHistory))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            LOG.error("Something went wrong on the prediction service!");
            throw e;
        } catch (Exception e) {
            LOG.error("Failed to connect to the prediction service! Make sure the ML service is running and can be accessed via network calls.");
            throw e;
        }
    }


    private String buildRequestUrl(double lat, double lon, double radius, Instant predictionDate) {
        return ML_SERVICE_URL +
                "/prediction?key=" +
                ML_ACCESS_KEY +
                "&prediction_date=" +
                predictionDate +
                "&lat=" +
                lat +
                "&lon=" +
                lon +
                "&radius=" +
                radius;
    }

}
