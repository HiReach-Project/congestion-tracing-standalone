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

    public void saveTrace(double lat, double lon, String deviceId, String companyKey) {
        GeometryFactory gf = new GeometryFactory();

        Point point = gf.createPoint(new Coordinate(lon, lat));
        point.setSRID(4326);

        deviceLocationRepository.save(DeviceLocation.builder()
                .location(point)
                .deviceId(deviceId)
                .updatedAt(Instant.now())
                .companyAccessKey(companyKey)
                .build());
    }
}
