package com.hireach.congestiontracing.controller;

import com.hireach.congestiontracing.service.DeviceLocationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api")
public class DeviceLocationController {

    private final DeviceLocationService deviceLocationService;

    public DeviceLocationController(DeviceLocationService deviceLocationService) {
        this.deviceLocationService = Objects.requireNonNull(deviceLocationService, "locationService should not be null");
    }

    @PostMapping(value = "/location")
    @ResponseStatus(HttpStatus.OK)
    public void saveTrace(@RequestParam(value = "lat") double lat,
                          @RequestParam(value = "lon") double lon,
                          @RequestParam(value = "device_id") String deviceId,
                          @RequestParam(value = "company_key") String companyKey) {
        deviceLocationService.saveDeviceLocation(lat, lon, deviceId, companyKey);
    }

    @GetMapping(value = "/congestion")
    @ResponseStatus(HttpStatus.OK)
    public int getCongestion(@RequestParam("lat") double lat,
                             @RequestParam(value = "lon") double lon,
                             @RequestParam(value = "radius") double radius) {
        return deviceLocationService.getCongestion(lat, lon, radius);
    }

}
