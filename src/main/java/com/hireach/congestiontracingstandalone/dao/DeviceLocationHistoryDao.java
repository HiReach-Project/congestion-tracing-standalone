package com.hireach.congestiontracingstandalone.dao;

import com.hireach.congestiontracingstandalone.model.MLDataModel;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DeviceLocationHistoryDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<MLDataModel> getHistory(Point point, Double radius) {
        return namedParameterJdbcTemplate.query("SELECT dateRounded as timestamp, count(DISTINCT device_id) AS value\n" +
                        "FROM (SELECT device_id,\n" +
                        "             CASE\n" +
                        "                 WHEN extract(SECONDS FROM updated_at) <= 30\n" +
                        "                     THEN (date_trunc('minute', updated_at) + INTERVAL '30 seconds')\n" +
                        "                 WHEN extract(SECONDS FROM updated_at) > 30\n" +
                        "                     THEN date_trunc('minute', updated_at + INTERVAL '30 seconds')\n" +
                        "                 END AS dateRounded\n" +
                        "      FROM device_location_history\n" +
                        "      WHERE ST_DistanceSphere(geometry(location_point), ST_PointFromText(:point)) < :radius\n" +
                        "     ) AS a\n" +
                        "GROUP BY dateRounded;",
                new MapSqlParameterSource()
                        .addValue("point", point.toText())
                        .addValue("radius", radius),
                crawlersOverviewDtoRowMapper());
    }

    private RowMapper<MLDataModel> crawlersOverviewDtoRowMapper() {
        return (rs, rowNum) -> new MLDataModel(
                rs.getObject("timestamp", OffsetDateTime.class),
                rs.getInt("value")
        );
    }

}
