package com.example.alertsystem.repository;

import com.example.alertsystem.model.ServiceArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceAreaRepository extends JpaRepository<ServiceArea, Long> {
    List<ServiceArea> findAll();
}

