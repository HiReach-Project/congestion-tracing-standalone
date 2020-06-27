package com.hireach.congestiontracing.service;

import com.hireach.congestiontracing.entity.Company;
import com.hireach.congestiontracing.entity.DeviceLocationHistory;
import com.hireach.congestiontracing.repository.DeviceLocationHistoryRepository;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

import static com.hireach.congestiontracing.util.Util.createPoint;

@Service
public class DeviceLocationHistoryService {

    private final DeviceLocationHistoryRepository deviceLocationHistoryRepository;

    public DeviceLocationHistoryService(DeviceLocationHistoryRepository deviceLocationHistoryRepository) {
        this.deviceLocationHistoryRepository = Objects.requireNonNull(deviceLocationHistoryRepository, "deviceLocationHistoryRepository should not be null");
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

}
