package com.example.prog2200_rest_api_spring_boot.service;
import com.example.prog2200_rest_api_spring_boot.repository.FireStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FireStationService {

    @Autowired
    FireStationRepository fireStationRepository;

    public FireStationService(FireStationRepository fireStationRepository) {
        this.fireStationRepository = fireStationRepository;
    }
    public Integer stationNumberByAddress(String address){
        return fireStationRepository.findStationNumberByAddress(address);
    }

}