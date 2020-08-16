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
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
        List<DeviceLocationHistoryRepository.MLDataModel> total = deviceLocationHistoryRepository.getHistory(point, 10D);

        System.out.println(Duration.between(start, Instant.now()));

        System.out.println(total.size());

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

        Instant start = Instant.now();

        Instant tempTime = Instant.now().minus(10, ChronoUnit.DAYS);

        while (tempTime.isBefore(Instant.now())) {

            LocalTime localTime = LocalTime.from(tempTime.atZone(ZoneId.of("UTC")));

            // if time is between 7-9 or 16-20 atunci 10-15 oameni
            if (localTime.isAfter(LocalTime.of(7, 0)) && localTime.isBefore(LocalTime.of(9, 0))
                    || localTime.isAfter(LocalTime.of(16, 0)) && localTime.isBefore(LocalTime.of(20, 0))) {
                createPoints(testCompany, tempTime, 16, 20);
            }
            // else if 9-16 atunci 5-8
            else if (localTime.isAfter(LocalTime.of(9, 0)) && localTime.isBefore(LocalTime.of(16, 0))) {
                createPoints(testCompany, tempTime, 5, 8);
            }
            // else if 20-23 random 0-4
            else if (localTime.isAfter(LocalTime.of(20, 0)) && localTime.isBefore(LocalTime.of(23, 0))) {
                createPoints(testCompany, tempTime, 0, 4);
            }
            // else random 0-1
            else {
                createPoints(testCompany, tempTime, 0, 2);
            }
            tempTime = tempTime.plusSeconds(30);
        }

        System.out.println(Duration.between(start, Instant.now()));
    }

    private void createPoints(Company testCompany, Instant tempTime, int lowLimit, int highLimit) {
        // 44.4133671, 26.1630280 - piata titan
        DecimalFormat df = new DecimalFormat("##.#######");
        int pointsToCreate = ThreadLocalRandom.current().nextInt(lowLimit, highLimit + 1);
        for (int i = 0; i < pointsToCreate; i++) {
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
