package com.example.alertsystem.service;

import com.example.alertsystem.model.Address;
import com.example.alertsystem.model.Person;
import com.example.alertsystem.model.PersonMedicalDetail_V;
import com.example.alertsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PersonService {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    ServiceAreaRepository serviceAreaRepository;
    @Autowired
    FireStationRepository fireStationRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    PersonMedicalDetailRepository personMedicalDetailRepository;

    /**
     * @param addressString String to search against address fields in Address table.
     * @return Returns address object associated with the address attribute.
     */
    public Address findAddress(String addressString) {
        // Address object to assign value to and return
        Address addressSearched = new Address();

        // List of addresses sourced from addressRepository.findAll() to get all address objects in DB.
        List<Address> allAddresses = new ArrayList<>(addressRepository.findAll());
        // Iterate through list and assign the object value to addressSearched
        for (Address address : allAddresses) {
            if (address.getAddress().equals(addressString)) {
                addressSearched = address;
            }
        }
        return addressSearched;
    }

    /**
     * @param addressString String to search against address fields in Address table.
     * @return Returns ArrayList of Person objects associated with the address searched
     */
    public List<Person> listOfPeopleAtAddress(String addressString) {
        // Get list of all addresses, match with the address string input by user.
        Address address = findAddress(addressString);
        // Use the findByAddress method in personRepository to get Person objects with the address returned
        // from findAddress(addressString).
        return new ArrayList<>(personRepository.findByAddress(address));
    }

    /**
     * @param stationNumber int to search against station number fields in Fire Station table
     * @return Returns a list of Person objects who are serviced by the Fire Station searched
     */
    public List<Person> findAllByStationNumber(int stationNumber) {
        return personRepository.findAllByAddress_Servicearea_FireStation_StationNumber(stationNumber);
    }

    /**
     * @return Returns list of all PersonMedicalDetail_V objects from PersonMedicalDetail_V view in DB
     */
    public List<PersonMedicalDetail_V> getAllPeopleAndMedicalDetails() {
        return personMedicalDetailRepository.findAll();
    }

    /**
     * @param city String to search against city fields in Address table
     * @return Returns list of all Person objects associated with the city specified
     */
    public List<Person> getListOfPeopleByCity(String city) {
        return personRepository.findAllByAddress_City(city);
    }

    /**
     * @param firstName String to search against fName fields in Person table
     * @param lastName  String to search against lName fields in Person table
     * @return Returns list of Person objects with the first and last name searched
     */
    public List<Person> getPersonByFNameLName(String firstName, String lastName) {
        return personRepository.findPeopleByFnameAndLname(firstName, lastName);
    }
}
