package com.hireach.congestiontracingstandalone.repository;

import com.hireach.congestiontracingstandalone.entity.DeviceLocationHistory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceLocationHistoryRepository extends JpaRepository<DeviceLocationHistory, Long> {
    @Query(value = "SELECT count(*)\n" +
            "FROM device_location_history\n" +
            "WHERE ST_DistanceSphere(geometry(location_point), ?1) < ?2", nativeQuery = true)
    int getHistory(Point point, Double radius);
}
