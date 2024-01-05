package com.example.alertsystem.repository;

import com.example.alertsystem.model.PersonMedicalDetail_V;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonMedicalDetailRepository extends JpaRepository<PersonMedicalDetail_V, Long> {
    List<PersonMedicalDetail_V> findAll();
}
