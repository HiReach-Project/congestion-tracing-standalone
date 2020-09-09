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
package com.hireach.congestiontracingstandalone.controller;

import com.hireach.congestiontracingstandalone.model.MLDataModel;
import com.hireach.congestiontracingstandalone.service.DeviceLocationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
@RequiredArgsConstructor
public class DeviceLocationHistoryController {

    private final DeviceLocationHistoryService deviceLocationHistoryService;

    @GetMapping(value = "/device-location-history")
    @ResponseStatus(HttpStatus.OK)
    public List<MLDataModel> getDeviceLocationHistory(@RequestParam("lat") @DecimalMin("-90.0") @DecimalMax("90.0") @Digits(integer = 2, fraction = 8) double lat,
                                                      @RequestParam("lon") @DecimalMin("-180.0") @DecimalMax("180.0") @Digits(integer = 3, fraction = 8) double lon,
                                                      @RequestParam("radius") @DecimalMin(value = "0.0", inclusive = false) @DecimalMax("6378000") double radius) {
        return deviceLocationHistoryService.getHistory(lat, lon, radius);
    }

    @GetMapping(value = "/prediction", produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public String predict(@RequestParam("lat") @DecimalMin("-90.0") @DecimalMax("90.0") @Digits(integer = 2, fraction = 8) double lat,
                          @RequestParam("lon") @DecimalMin("-180.0") @DecimalMax("180.0") @Digits(integer = 3, fraction = 8) double lon,
                          @RequestParam(value = "radius", required = false) @DecimalMin(value = "0.0", inclusive = false) @DecimalMax("6378000") Double radius,
                          @RequestParam("prediction_date") Instant predictionDate) {
        return deviceLocationHistoryService.getPrediction(lat, lon, (radius == null ? 11 : radius), predictionDate);
    }

}
