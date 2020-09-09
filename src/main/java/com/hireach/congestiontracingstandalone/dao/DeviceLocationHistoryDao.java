//    Congestion API - a REST API built to track congestion spots and
//    crowded areas using real-time location data from mobile devices.
//
//    Copyright (C) 2020, University Politehnica of Bucharest, member
//    of the HiReach Project consortium <https://hireach-project.eu/>
//    <andrei[dot]gheorghiu[at]upb[dot]ro. This project has received
//    funding from the European Union’s Horizon 2020 research and
//    innovation programme under grant agreement no. 769819.
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
