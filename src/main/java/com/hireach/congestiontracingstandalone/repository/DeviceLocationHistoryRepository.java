package com.hireach.congestiontracingstandalone.repository;

import com.hireach.congestiontracingstandalone.entity.DeviceLocationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceLocationHistoryRepository extends JpaRepository<DeviceLocationHistory, Long> {
}
