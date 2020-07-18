package com.hireach.congestiontracingstandalone.controller;

import com.hireach.congestiontracingstandalone.component.CompanyWrapper;
import com.hireach.congestiontracingstandalone.service.DeviceLocationHistoryService;
import com.hireach.congestiontracingstandalone.service.DeviceLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeviceLocationController {

    private final DeviceLocationService deviceLocationService;
    private final DeviceLocationHistoryService deviceLocationHistoryService;
    private final CompanyWrapper companyWrapper;

    @PostMapping(value = "/location")
    @ResponseStatus(HttpStatus.OK)
    public void saveDeviceLocation(@RequestParam(value = "lat") double lat,
                                   @RequestParam(value = "lon") double lon,
                                   @RequestParam(value = "device_id") String deviceId) {
        Instant instant = Instant.now();
        deviceLocationService.saveOrUpdateDeviceLocation(lat, lon, deviceId, companyWrapper.getCompany(), instant);
        deviceLocationHistoryService.saveDeviceLocationHistory(lat, lon, deviceId, companyWrapper.getCompany(), instant);
    }

    @GetMapping(value = "/congestion")
    @ResponseStatus(HttpStatus.OK)
    public int getCongestion(@RequestParam("lat") double lat,
                             @RequestParam(value = "lon") double lon,
                             @RequestParam(value = "radius") double radius,
                             @RequestParam(value = "seconds_ago", required = false) Integer secondsAgo) {
        return deviceLocationService.getCongestion(lat, lon, radius, secondsAgo);
    }

}
