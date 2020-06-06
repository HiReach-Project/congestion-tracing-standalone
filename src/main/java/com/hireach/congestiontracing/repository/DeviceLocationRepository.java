package com.hireach.congestiontracing.repository;

import com.hireach.congestiontracing.entity.DeviceLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceLocationRepository extends JpaRepository<DeviceLocation, Long> {

}
