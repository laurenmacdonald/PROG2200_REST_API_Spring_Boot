package com.example.prog2200_rest_api_spring_boot.repository;

import com.example.prog2200_rest_api_spring_boot.model.ServiceArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceAreaRepository extends JpaRepository<ServiceArea, Long> {
    List<ServiceArea> findAll();
}

