package com.hireach.congestiontracingstandalone.service;

import com.hireach.congestiontracingstandalone.entity.Company;
import com.hireach.congestiontracingstandalone.entity.DeviceLocationHistory;
import com.hireach.congestiontracingstandalone.repository.DeviceLocationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

import static com.hireach.congestiontracingstandalone.util.Util.createPoint;

@Service
@RequiredArgsConstructor
public class DeviceLocationHistoryService {

    private final DeviceLocationHistoryRepository deviceLocationHistoryRepository;

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
