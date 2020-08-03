package com.hireach.congestiontracingstandalone;

import com.hireach.congestiontracingstandalone.entity.Company;
import com.hireach.congestiontracingstandalone.repository.CompanyRepository;
import com.hireach.congestiontracingstandalone.repository.DeviceLocationHistoryRepository;
import com.hireach.congestiontracingstandalone.service.DeviceLocationHistoryService;
import com.hireach.congestiontracingstandalone.service.DeviceLocationService;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

import static com.hireach.congestiontracingstandalone.util.Util.createPoint;

@SpringBootTest
class CongestionTracingApplicationTests {

    @Autowired
    DeviceLocationService deviceLocationService;

    @Autowired
    DeviceLocationHistoryService deviceLocationHistoryService;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    DeviceLocationHistoryRepository deviceLocationHistoryRepository;

    @Test
    public void getHistory() {
        Instant start = Instant.now();

        Point point = createPoint(44.4133671, 26.1630280);
        int total = deviceLocationHistoryRepository.getHistory(point, 1000D);
        System.out.println(Duration.between(start, Instant.now()));

        System.out.println(total);

    }

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

    @Test
    @Rollback(value = false)
    void simulateDeviceLocationsInBusStops() {
        Company testCompany = companyRepository.findAll().get(0);
        Point point = createPoint(44.4133000, 26.1630000);

        Instant start = Instant.now();

        Instant tempTime = Instant.now().minus(2, ChronoUnit.DAYS);

        while (tempTime.isBefore(Instant.now())) {
            // if time is between 7-9 or 16-20 atunci 10-15 oameni
            // else if 9-16 atunci 5-8
            // else if 20-23 random 0-4
            // else random 0-1
            createPoints(testCompany, tempTime);
            tempTime = tempTime.plusSeconds(30);
        }

        System.out.println(Duration.between(start, Instant.now()));

    }

    private void createPoints(Company testCompany, Instant tempTime) {
        DecimalFormat df = new DecimalFormat("##.#######");
        for (int i = 0; i < 1000; i++) {
            double randomLat = ThreadLocalRandom.current().nextDouble(44.4133000, 44.4134000);
            double randomLon = ThreadLocalRandom.current().nextDouble(26.1630000, 26.1631000);

            deviceLocationHistoryService.saveDeviceLocationHistory(
                    Double.parseDouble(df.format(randomLat)),
                    Double.parseDouble(df.format(randomLon)),
                    "deviceId" + i,
                    testCompany,
                    tempTime
            );
        }
    }

}
