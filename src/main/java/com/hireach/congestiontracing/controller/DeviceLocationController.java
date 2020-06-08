package com.hireach.congestiontracing.controller;

import com.hireach.congestiontracing.service.DeviceLocationHistoryService;
import com.hireach.congestiontracing.service.DeviceLocationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class DeviceLocationController {

    private final DeviceLocationService deviceLocationService;

    private final DeviceLocationHistoryService deviceLocationHistoryService;

    public DeviceLocationController(DeviceLocationService deviceLocationService, DeviceLocationHistoryService deviceLocationHistoryService) {
        this.deviceLocationService = Objects.requireNonNull(deviceLocationService, "deviceLocationService should not be null");
        this.deviceLocationHistoryService = Objects.requireNonNull(deviceLocationHistoryService, "deviceLocationHistoryService should not be null");
    }

    @PostMapping(value = "/location")
    @ResponseStatus(HttpStatus.OK)
    public void saveDeviceLocation(@RequestParam(value = "lat") double lat,
                                   @RequestParam(value = "lon") double lon,
                                   @RequestParam(value = "device_id") String deviceId,
                                   @RequestParam(value = "company_key") String companyKey) {
        Instant instant = Instant.now();
        deviceLocationService.saveOrUpdateDeviceLocation(lat, lon, deviceId, companyKey, instant);
        deviceLocationHistoryService.saveDeviceLocationHistory(lat, lon, deviceId, companyKey, instant);
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
