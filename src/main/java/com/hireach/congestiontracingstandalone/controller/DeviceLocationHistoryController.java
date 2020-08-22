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

    @GetMapping(value = "/device_location_history")
    @ResponseStatus(HttpStatus.OK)
    public List<MLDataModel> getDeviceLocationHistory(@RequestParam("lat") @DecimalMin("-90.0") @DecimalMax("90.0") @Digits(integer = 2, fraction = 8) double lat,
                                                      @RequestParam("lon") @DecimalMin("-180.0") @DecimalMax("180.0") @Digits(integer = 3, fraction = 8) double lon,
                                                      @RequestParam("radius") @DecimalMin(value = "0.0", inclusive = false) @DecimalMax("6378000") double radius) {
        return deviceLocationHistoryService.getHistory(lat, lon, radius);
    }

    @GetMapping(value = "/predict", produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public String predict(@RequestParam("lat") @DecimalMin("-90.0") @DecimalMax("90.0") @Digits(integer = 2, fraction = 8) double lat,
                          @RequestParam("lon") @DecimalMin("-180.0") @DecimalMax("180.0") @Digits(integer = 3, fraction = 8) double lon,
                          @RequestParam(value = "radius", required = false) @DecimalMin(value = "0.0", inclusive = false) @DecimalMax("6378000") Double radius,
                          @RequestParam("prediction_date") Instant predictionDate) {
        return deviceLocationHistoryService.getPrediction(lat, lon, (radius == null ? 11 : radius), predictionDate);
    }

}
