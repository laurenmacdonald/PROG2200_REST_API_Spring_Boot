package com.example.alertsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/* Creating entity, connecting to view named person_medical_detail_v in alertsystem MySQL database.
 * This view is created by connecting the Person table with junction tables person_allergy and person_medication,
 * to join with the allergies and medications tables. Address is also joined on to return Person details, their address,
 * and medications and/or allergies. If no allergies or medications, will be null. There is one record per person in the
 * table. If multiple allergies or medications, they are concatenated to one String value separated by commas.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="person_medical_detail_v")
public class PersonMedicalDetail_V {
    @Id
    private long person_id;
    @Column(name = "fname")
    private String fname;
    @Column(name = "lname")
    private String lname;
    @Column(name = "age")
    private int age;
    @Column(name = "phone")
    private String phone;
    @Column(name = "medications")
    private String medications;
    @Column(name="allergies")
    private String allergies;
    @Column(name="address")
    private String address;

}