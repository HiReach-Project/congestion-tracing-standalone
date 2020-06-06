package com.hireach.congestiontracing.controller;

import com.hireach.congestiontracing.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/api")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = Objects.requireNonNull(locationService, "locationService should not be null");
    }

    @PostMapping(value = "/location")
    @ResponseStatus(HttpStatus.OK)
    public void saveTrace(@RequestParam(value = "lat") double lat,
                          @RequestParam(value = "lon") double lon,
                          @RequestParam(value = "device_id") String deviceId,
                          @RequestParam(value = "company_key") String companyKey) {
        locationService.saveTrace(lat, lon, deviceId, companyKey);
    }

    @GetMapping(value = "/congestion")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getCongestion(@RequestParam("lat") double lat,
                                      @RequestParam(value = "lon") double lon,
                                      @RequestParam(value = "r") double radius) {
        return Collections.emptyList();
    }

}
