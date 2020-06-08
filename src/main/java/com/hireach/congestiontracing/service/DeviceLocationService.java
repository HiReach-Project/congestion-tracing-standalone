package com.hireach.congestiontracing.service;

import com.hireach.congestiontracing.entity.DeviceLocation;
import com.hireach.congestiontracing.repository.DeviceLocationRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
public class DeviceLocationService {

    DeviceLocationRepository deviceLocationRepository;

    public DeviceLocationService(DeviceLocationRepository deviceLocationRepository) {
        this.deviceLocationRepository = Objects.requireNonNull(deviceLocationRepository, "locationRepository should not be null");
    }

    public void saveDeviceLocation(double lat, double lon, String deviceId, String companyKey) {
        Point point = createPoint(lat, lon);

        deviceLocationRepository.save(DeviceLocation.builder()
                .locationPoint(point)
                .deviceId(deviceId)
                .updatedAt(Instant.now())
                .companyAccessKey(companyKey)
                .build());
    }

    public int getCongestion(double lat, double lon, double radius, Integer secondsAgo) {
        Point point = createPoint(lat, lon);

        return deviceLocationRepository.getCongestion(point, radius, (secondsAgo == null ? 30 : secondsAgo));
    }

    private Point createPoint(double lat, double lon) {
        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new Coordinate(lon, lat));
        point.setSRID(4326);

        return point;
    }

}
