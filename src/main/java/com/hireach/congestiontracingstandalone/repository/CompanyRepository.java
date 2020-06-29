package com.hireach.congestiontracingstandalone.repository;

import com.hireach.congestiontracingstandalone.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByAccessKey(String hashedKey);

}
