package com.hireach.congestiontracing.repository;

import com.hireach.congestiontracing.entity.DeviceLocationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceLocationHistoryRepository extends JpaRepository<DeviceLocationHistory, Long> {
}
