package com.example.prog2200_rest_api_spring_boot.repository;

import com.example.prog2200_rest_api_spring_boot.model.PersonMedicalDetail_V;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonMedicalDetailRepository extends JpaRepository<PersonMedicalDetail_V, Long> {
    List<PersonMedicalDetail_V> findAll();
}
