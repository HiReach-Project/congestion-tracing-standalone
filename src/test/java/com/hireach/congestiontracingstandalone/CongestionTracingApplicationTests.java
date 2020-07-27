package com.hireach.congestiontracingstandalone;

import com.hireach.congestiontracingstandalone.entity.Company;
import com.hireach.congestiontracingstandalone.repository.CompanyRepository;
import com.hireach.congestiontracingstandalone.service.DeviceLocationHistoryService;
import com.hireach.congestiontracingstandalone.service.DeviceLocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
class CongestionTracingApplicationTests {

    @Autowired
    DeviceLocationService deviceLocationService;

    @Autowired
    DeviceLocationHistoryService deviceLocationHistoryService;

    @Autowired
    CompanyRepository companyRepository;

    @Test
    @Rollback(value = false)
    void simulateDeviceLocations() {
        Company testCompany = companyRepository.findAll().get(0);
        DecimalFormat df = new DecimalFormat("##.######");
        // stanga sus 44.480239, 26.027606
        // dreapta sus 44.480239, 26.148456
        // dreapta jos 44.397630, 26.148456
        // stanga jos 44.397630, 26.027606
        Instant start = Instant.now();

        Instant tempTime = Instant.now().minus(2, ChronoUnit.DAYS);

        while (tempTime.isBefore(Instant.now())) {
            for (int i = 0; i < 1000; i++) {
                double randomLat = ThreadLocalRandom.current().nextDouble(44.397630, 44.480239);
                double randomLon = ThreadLocalRandom.current().nextDouble(26.027606, 26.148456);

                deviceLocationHistoryService.saveDeviceLocationHistory(
                        Double.parseDouble(df.format(randomLat)),
                        Double.parseDouble(df.format(randomLon)),
                        "deviceId" + i,
                        testCompany,
                        tempTime
                );
            }
            tempTime = tempTime.plusSeconds(30);
        }

        System.out.println(Duration.between(start, Instant.now()));

    }

}
