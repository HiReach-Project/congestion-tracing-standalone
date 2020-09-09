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
package com.hireach.congestiontracingstandalone.repository;

import com.hireach.congestiontracingstandalone.entity.DeviceLocation;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceLocationRepository extends JpaRepository<DeviceLocation, Long> {

    @Query(value = "SELECT count(*)\n" +
            "FROM device_location\n" +
            "WHERE updated_at > current_timestamp at time zone 'utc' - ?3 * interval '1 seconds'\n" +
            "  AND ST_DistanceSpheroid(geometry(location_point), ?1,\n" +
            "                          'SPHEROID[\"WGS 84\",6378137,298.257223563]') < ?2", nativeQuery = true)
    int getCongestion(Point point, Double radius, Integer secondsAgo);

}
