package com.hireach.congestiontracingstandalone.repository;

import com.hireach.congestiontracingstandalone.entity.DeviceLocationHistory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceLocationHistoryRepository extends JpaRepository<DeviceLocationHistory, Long> {
    @Query(value = "select rounded_date,  count(distinct device_id) from\n" +
            "(SELECT id,\n" +
            "       company_id,\n" +
            "       device_id,\n" +
            "       location_point,\n" +
            "       case\n" +
            "           when extract(SECONDS from updated_at) <= 30\n" +
            "               then (date_trunc('minute', updated_at) + interval '30 seconds')\n" +
            "           when extract(SECONDS from updated_at) > 30\n" +
            "               then date_trunc('minute', updated_at + interval '30 seconds')\n" +
            "           end rounded_date\n" +
            "FROM device_location_history\n" +
            "WHERE ST_DistanceSphere(geometry(location_point), ?1) < ?2\n" +
            "    ) as a\n" +
            "group by rounded_date", nativeQuery = true)
    int getHistory(Point point, Double radius);
}
