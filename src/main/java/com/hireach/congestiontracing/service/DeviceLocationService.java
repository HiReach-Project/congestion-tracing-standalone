package com.hireach.congestiontracing.service;

import com.hireach.congestiontracing.entity.DeviceLocation;
import com.hireach.congestiontracing.repository.DeviceLocationHistoryRepository;
import com.hireach.congestiontracing.repository.DeviceLocationRepository;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

import static com.hireach.congestiontracing.util.Util.createPoint;

@Service
public class DeviceLocationService {

    DeviceLocationRepository deviceLocationRepository;

    public DeviceLocationService(DeviceLocationRepository deviceLocationRepository, DeviceLocationHistoryRepository deviceLocationHistoryRepository) {
        this.deviceLocationRepository = Objects.requireNonNull(deviceLocationRepository, "deviceLocationRepository should not be null");
    }

    public void saveOrUpdateDeviceLocation(double lat, double lon, String deviceId, String companyKey, Instant instant) {
        Point point = createPoint(lat, lon);

        deviceLocationRepository.save(DeviceLocation.builder()
                .locationPoint(point)
                .deviceId(deviceId)
                .updatedAt(instant)
                .companyAccessKey(companyKey)
                .build());
    }

    public int getCongestion(double lat, double lon, double radius, Integer secondsAgo) {
        Point point = createPoint(lat, lon);

        return deviceLocationRepository.getCongestion(point, radius, (secondsAgo == null ? 30 : secondsAgo));
    }

}
