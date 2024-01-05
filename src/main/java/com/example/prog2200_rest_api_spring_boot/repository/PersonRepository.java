package com.example.prog2200_rest_api_spring_boot.repository;


import com.example.prog2200_rest_api_spring_boot.model.Address;
import com.example.prog2200_rest_api_spring_boot.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findAll();
    List<Person> findByAddress(Address address);
    List<Person> findAllByAddress_Servicearea_FireStation_StationNumber(int station_number);
    List<Person> findAllByAddress_City(String city);
    List<Person> findPeopleByFnameAndLname(String firstName, String lastName);


}
