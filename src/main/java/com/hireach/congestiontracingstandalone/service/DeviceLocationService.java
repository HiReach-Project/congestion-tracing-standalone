package com.hireach.congestiontracingstandalone.service;

import com.hireach.congestiontracingstandalone.entity.Company;
import com.hireach.congestiontracingstandalone.entity.DeviceLocation;
import com.hireach.congestiontracingstandalone.repository.DeviceLocationRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.hireach.congestiontracingstandalone.util.Util.createPoint;

@Service
@RequiredArgsConstructor
public class DeviceLocationService {

    private final DeviceLocationRepository deviceLocationRepository;

    public void saveOrUpdateDeviceLocation(double lat, double lon, String deviceId, Company company, Instant instant) {
        Point point = createPoint(lat, lon);

        deviceLocationRepository.save(DeviceLocation.builder()
                .locationPoint(point)
                .deviceId(deviceId)
                .updatedAt(instant)
                .company(company)
                .build());
    }

    public int getCongestion(double lat, double lon, double radius, Integer secondsAgo) {
        Point point = createPoint(lat, lon);
        return deviceLocationRepository.getCongestion(point, radius, (secondsAgo == null ? 30 : secondsAgo));
    }

}
