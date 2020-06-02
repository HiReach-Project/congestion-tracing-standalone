package com.hireach.congestiontracing.repository;

import com.hireach.congestiontracing.entity.Trace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraceRepository extends JpaRepository<Trace, Long> {

}
