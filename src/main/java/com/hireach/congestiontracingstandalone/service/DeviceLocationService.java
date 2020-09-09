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
package com.hireach.congestiontracingstandalone.service;

import com.hireach.congestiontracingstandalone.entity.Company;
import com.hireach.congestiontracingstandalone.entity.DeviceLocation;
import com.hireach.congestiontracingstandalone.repository.DeviceLocationRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.hireach.congestiontracingstandalone.util.Util.createPoint;

@Service
@RequiredArgsConstructor
public class DeviceLocationService {

    private final DeviceLocationRepository deviceLocationRepository;

    public void saveOrUpdateDeviceLocation(double lat, double lon, String deviceId, Company company, Instant instant) {
        Point point = createPoint(lat, lon);

        deviceLocationRepository.save(DeviceLocation.builder()
                .locationPoint(point)
                .deviceId(deviceId)
                .updatedAt(instant)
                .company(company)
                .build());
    }

    public int getCongestion(double lat, double lon, double radius, Integer secondsAgo) {
        Point point = createPoint(lat, lon);
        return deviceLocationRepository.getCongestion(point, radius, (secondsAgo == null ? 30 : secondsAgo));
    }

}
