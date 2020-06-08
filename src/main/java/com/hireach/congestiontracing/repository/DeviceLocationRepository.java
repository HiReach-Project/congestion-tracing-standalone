package com.hireach.congestiontracing.repository;

import com.hireach.congestiontracing.entity.DeviceLocation;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceLocationRepository extends JpaRepository<DeviceLocation, Long> {

    @Query(value = "SELECT count(*)\n" +
            "FROM device_location\n" +
            "WHERE updated_at > current_timestamp - ?3 * interval '1 seconds'\n" +
            "  AND ST_DistanceSpheroid(geometry(location_point), ?1,\n" +
            "                          'SPHEROID[\"WGS 84\",6378137,298.257223563]') < ?2", nativeQuery = true)
    int getCongestion(Point point, Double radius, Integer secondsAgo);

}
