package com.hireach.congestiontracingstandalone.repository;

import com.hireach.congestiontracingstandalone.entity.DeviceLocationHistory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface DeviceLocationHistoryRepository extends JpaRepository<DeviceLocationHistory, Long> {

    interface MLDataModel {
         Instant getDateRounded();
         Integer getDevices();
    }

    @Query(value = "SELECT date_rounded, count(DISTINCT device_id) AS devices\n" +
            "FROM (SELECT device_id,\n" +
            "             CASE\n" +
            "                 WHEN extract(SECONDS FROM updated_at) <= 30\n" +
            "                     THEN (date_trunc('minute', updated_at) + INTERVAL '30 seconds')\n" +
            "                 WHEN extract(SECONDS FROM updated_at) > 30\n" +
            "                     THEN date_trunc('minute', updated_at + INTERVAL '30 seconds')\n" +
            "                 END AS date_rounded\n" +
            "      FROM device_location_history\n" +
            "      WHERE ST_DistanceSphere(geometry(location_point), ?1) < ?2\n" +
            "     ) AS a\n" +
            "GROUP BY date_rounded;", nativeQuery = true)
    List<MLDataModel> getHistory(Point point, Double radius);
}
