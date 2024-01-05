package com.example.prog2200_rest_api_spring_boot.repository;

import com.example.prog2200_rest_api_spring_boot.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAll();
}