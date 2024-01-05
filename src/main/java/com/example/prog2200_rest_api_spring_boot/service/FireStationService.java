package com.example.alertsystem.service;

import com.example.alertsystem.repository.FireStationRepository;
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