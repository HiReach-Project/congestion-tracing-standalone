package com.hireach.congestiontracing.service;

import com.hireach.congestiontracing.entity.Trace;
import com.hireach.congestiontracing.repository.TraceRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TraceService {

    @Autowired
    TraceRepository traceRepository;

    public void saveTrace() {
        GeometryFactory gf = new GeometryFactory();
        Double lat = 44.4268123456789;
        Double lon = 26.1025432123456;
        Point point = gf.createPoint(new Coordinate(lat, lon));
        point.setSRID(4326);

        System.out.println(point);

        traceRepository.save(Trace.builder()
                .location(point)
                .deviceId("deviceid")
                .updatedAt(Instant.now())
                .companyAccessKey("sq")
                .build());
    }
}
