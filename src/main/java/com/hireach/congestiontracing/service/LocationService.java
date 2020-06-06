package com.hireach.congestiontracing.service;

import com.hireach.congestiontracing.entity.Location;
import com.hireach.congestiontracing.repository.LocationRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
public class LocationService {

    LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = Objects.requireNonNull(locationRepository, "locationRepository should not be null");
    }

    public void saveTrace(double lat, double lon, String deviceId, String companyKey) {
        GeometryFactory gf = new GeometryFactory();

        Point point = gf.createPoint(new Coordinate(lat, lon));
        point.setSRID(4326);

        locationRepository.save(Location.builder()
                .location(point)
                .deviceId(deviceId)
                .updatedAt(Instant.now())
                .companyAccessKey(companyKey)
                .build());
    }
}
