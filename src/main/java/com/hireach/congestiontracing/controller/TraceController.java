package com.hireach.congestiontracing.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class TraceController {

    @PostMapping(value = "/v2/proxies")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getProxies(@RequestParam("lat") Double lat,
                                   @RequestParam(value = "location") Double lon) {
        return Collections.emptyList();
    }
}
