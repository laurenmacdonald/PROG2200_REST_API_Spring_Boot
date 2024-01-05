package com.example.alertsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* Creating entity, connecting to table named person in alertsystem MySQL database. OneToOne relationship with the address table
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long person_id;
    @Column(name = "fname")
    private String fname;
    @Column(name = "lname")
    private String lname;
    @Column(name = "dob")
    private String dob;
    @Column(name = "age")
    private int age;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;

    @OneToOne
    @JoinColumn(name="address_id", referencedColumnName = "address_id")
    private Address address;


}