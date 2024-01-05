package com.example.alertsystem.controller;

import com.example.alertsystem.model.Person;
import com.example.alertsystem.model.PersonMedicalDetail_V;
import com.example.alertsystem.service.FireStationService;
import com.example.alertsystem.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class PersonController {
    @Autowired
    private PersonService personService;

    @Autowired
    private FireStationService fireStationService;

    /**
     * @param person Person object
     * @return
     * Returns a HashMap that holds key value pairs for first name, last name and age of the
     * person specified.
     */
    public Map<String,String> makeChildAlertMap(Person person){
        Map<String, String> personMap = new HashMap<>();
        personMap.put("fName", person.getFname());
        personMap.put("lName", person.getLname());
        personMap.put("age", Integer.toString(person.getAge()));
        return personMap;
    }

    /**
     * @param peopleAtAddress List of people at the address
     * @return
     * Returns a List of person_ids associated with the address
     */
    public List<Long> getListOfIds(List<Person> peopleAtAddress){
        // Set up ArrayList to get the ids of the people at the address
        List<Long> personIdList = new ArrayList<>();
        long personId;
        // Iterate through the peopleAtAddress, get their person_id and store it in personIdList
        for (Person persons: peopleAtAddress) {
            personId = persons.getPerson_id();
            personIdList.add(personId);
        }
        return personIdList;
    }

    /**
     * @param person PersonMedicalDetail_V object
     * @param personMap Map to store data from person object
     * @return
     * Returns a Map that holds key value pairs for first name, last name, phone, age, medications and allergies
     */
    private Map<String, String> getPersonMap(PersonMedicalDetail_V person, Map<String, String> personMap) {
        personMap.put("fName", person.getFname());
        personMap.put("lName", person.getLname());
        personMap.put("phone", person.getPhone());
        personMap.put("age", String.valueOf(person.getAge()));
        personMap.put("medications", person.getMedications());
        personMap.put("allergies", person.getAllergies());
        return personMap;
    }

    /**
     * @param stationNumber station number associated with fire station
     * @return
     * Returns a list of people serviced by the corresponding fire station, including first & last name, address, phone #.
     * Also returns a summary of number of adults and number of children (aged 18 and younger)
     *
     */
    @GetMapping("/firestation")
    public Map<String, Object> findPeopleServicedByFireStation(@RequestParam int stationNumber){
        // Get a list of people associated with station number provided.
        List<Person> peopleServicedByStation = personService.findAllByStationNumber(stationNumber);

        // Create a list of HashMaps to store and return the information required.
        // Stream() is used to retrieve only the information required for the response, map used to
        // map the values with a key. Sorted sorts the elements by address. Collect stores this data as a list
        // in servicedPeople.
        List<Map<String, String>> servicedPeople = peopleServicedByStation.stream()
                .map(person -> {
                    Map<String, String> personMap = new HashMap<>();
                    personMap.put("fName", person.getFname());
                    personMap.put("lName", person.getLname());
                    personMap.put("address", person.getAddress().getAddress());
                    personMap.put("phone", person.getPhone());
                    return personMap;
                })
                .sorted(Comparator.comparing(personMap -> personMap.get("address")))
                .collect(Collectors.toList());

        // Finding the number of adults and kids, storing in a LinkedHashMap
        long numAdults = peopleServicedByStation.stream().filter(person -> person.getAge() > 18).count();
        long numKids = peopleServicedByStation.size() - numAdults;

        Map<String, Long> demographicsNumbers = new LinkedHashMap<>();
        demographicsNumbers.put("numKids", numKids);
        demographicsNumbers.put("numAdults", numAdults);

        // Creating a LinkedHashMap that takes a String for the key and an Object for the value. The servicedPeople
        // List of HashMaps is stored as object, as well as the demographic (age) information.
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("people", servicedPeople);
        response.put("demographicsNumbers", demographicsNumbers);

        return response;
    }

    /**
     * @param address address of household
     * @return
     * Returns a list LinkedHashMaps. One LinkedHashMap = Children at the address specified: Includes first & last name.
     * Other LinkedHasHMap =  all other persons living at the address. If no children, returns an empty string.
     */
    @GetMapping("/childAlert")
    public Map<String, List<Map<String,String>>> findChildrenByAddress(@RequestParam String address){
        // List of Person objects to store the list of people at the specified address.
        List<Person> peopleAtAddress = personService.listOfPeopleAtAddress(address);

        // List of LinkedHashMaps to store the adults at the address. Using stream() to filter (>18) and map (calling the
        // makeChildAlertMap to do so), storing the stream data as a list.
        List<Map<String, String>> adults = peopleAtAddress.stream()
                .filter(person -> person.getAge() > 18)
                .map(this::makeChildAlertMap)
                .collect(Collectors.toList());

        // Doing the same for kids, filtering for person objects with age <= 18.
        List<Map<String, String>> kids = peopleAtAddress.stream()
                .filter(person -> person.getAge() <= 18)
                .map(this::makeChildAlertMap)
                .collect(Collectors.toList());

        // Creating a LinkedHashMap to store key as a String and value as a List of HashMaps (adults or children)
        Map<String, List<Map<String,String>>> household = new LinkedHashMap<>();
        household.put("kids", kids);
        household.put("adults", adults);
        // If kids is empty, return an empty String
        if(kids.isEmpty()){
            household.clear();
        }
        return household;
    }

    /**
     * @param firestation station number of the fire station
     * @return
     * Returns a list of phone numbers of each person within the fire station's jurisdiction.
     */
    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumbers(@RequestParam int firestation){
        // List of Person objects filled with people who are serviced by the fire station.
        List<Person> peopleServicedByStation = personService.findAllByStationNumber(firestation);
        // List of phone numbers
        List<String> phoneNumbers;

        // Using stream and map to get the phone number of each Person object in the personList, store in the phoneNumbers List
        phoneNumbers = peopleServicedByStation.stream()
                .map(Person::getPhone)
                .collect(Collectors.toList());

        return phoneNumbers;
    }

    /**
     * @param address address to search fore fire station that service's it.
     * @return
     * Returns the fire station number that services the provided address as well as a list of all the people living at
     * the address. Includes person's name, phone number, age, medications with dosage and allergies.
     */
    @GetMapping("/fire")
    public Map<String, Object> getStationNumberAndPeople(@RequestParam String address){
        Integer stationNumber = fireStationService.stationNumberByAddress(address);

        // Get a list of Person objects that are associated with the given address.
        List<Person> personByAddress = personService.listOfPeopleAtAddress(address);
        // Get the list of all Person's medical details from the medical detail view
        List<PersonMedicalDetail_V> personMedicalDetailVList = personService.getAllPeopleAndMedicalDetails();
        // Get list of Ids to filter by
        List<Long> personIdList = getListOfIds(personByAddress);
        // Create a list of people and their medical details who live at the address by filtering for id.
        List<Map<String, String>> personMedicalDetailAtAddress = personMedicalDetailVList.stream()
                .filter(person -> personIdList.contains(person.getPerson_id()))
                .map(person -> {
                    Map<String, String> personMap = new LinkedHashMap<>();
                    return getPersonMap(person, personMap);
                })
                .collect(Collectors.toList());

        // Created a new LinkedHashMap to store key value pair (String for key, Object for value)
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("stationNumber", stationNumber);
        resultMap.put("listOfPeople", personMedicalDetailAtAddress);

        return resultMap;
    }


    /**
     * @param stationNumbers takes a list of station numbers
     * @return
     * Returns a Map of all the households in each fire station's jurisdiction. List is grouped by the address. Includes
     * name, phone, age, medications and allergies.
     */
    @GetMapping("/flood")
    public Map<Object, List<Map<String, String>>> getAllInfoInArea(@RequestParam List<Integer> stationNumbers){
        // Create an ArrayList to hold Person objects associated with the stationNumbers provided.
        List<Person> peopleServicedByStation = new ArrayList<>();

        // For each loop to iterate through list of stationNumbers from parameter, find Person objects serviced by the station
        // and store these objects in a temporary list. Then add this list of objects to the peopleServicedByStation list.
        for (int stationNumber : stationNumbers) {
            List<Person> atStationNumber = personService.findAllByStationNumber(stationNumber);
            peopleServicedByStation.addAll(atStationNumber);
        }

        // Get the full list of people and their medical details.
        List<PersonMedicalDetail_V> personMedicalDetailVList = personService.getAllPeopleAndMedicalDetails();
        // Get list of Ids to filter personMedicalDetailVList by, using the getListOfIds method and supplying
        // peopleServicedByStation.
        List<Long> personIdList = getListOfIds(peopleServicedByStation);

        // Filter the personMedicalDetailVList by ids of those serviced by station number and group it by address.
        return personMedicalDetailVList.stream()
                .filter(person -> personIdList.contains(person.getPerson_id()))
                .map(person -> {
                    Map<String, String> personMap = new LinkedHashMap<>();
                    personMap.put("address", person.getAddress());
                    return getPersonMap(person, personMap);
                })
                .collect(Collectors.groupingBy(
                        personMap-> personMap.get("address"),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    /**
     * @param firstName String first name of person
     * @param lastName String last name of person
     * @return
     * Returns the person's name, address, age, email, list of medications and allergies.
     */
    @GetMapping("/personInfo")
    public List<PersonMedicalDetail_V> getPersonInfo(@RequestParam String firstName, @RequestParam String lastName){
        // Get a list of Person objects with the first and last name provided.
        List<Person> personList = personService.getPersonByFNameLName(firstName, lastName);
        // Get list of all people and their medical details
        List<PersonMedicalDetail_V> personMedicalDetailVList = personService.getAllPeopleAndMedicalDetails();
        // Crete a list to hold personIds and long to hold individual ids
        List<Long> personIdList = new ArrayList<>();
        long personId;
        // for each loop to iterate through the personList and get their ids, storing in personIdList
        for (Person persons: personList) {
            personId = persons.getPerson_id();
            personIdList.add(personId);
        }
        // Create a list of people and their medical details who live at the address by filtering for id in personIdList.
        return personMedicalDetailVList.stream()
                .filter(person -> personIdList.contains(person.getPerson_id()))
                .collect(Collectors.toList());
    }

    /**
     * @param city String city to search
     * @return
     * Returns the email addresses of all the people in the city.
     */
    @GetMapping("/communityEmail")
    public List<String> getEmailAddresses(@RequestParam String city){
        // Create a list of all people in the city specified
        List<Person> peopleInCity = personService.getListOfPeopleByCity(city);
        // List to hold email addresses
        List<String> emailAddresses;

        // Use stream, map and collect methods to store the list of emails from the personList in the emailList.
        emailAddresses = peopleInCity.stream()
                .map(Person::getEmail)
                .collect(Collectors.toList());

        return emailAddresses;
    }

}