package com.example.alertsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/* Creating entity, connecting to table named address in alertsystem MySQL database. OneToOne relationship used between
   this class and the ServiceArea class
*/
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="address")
public class Address {
    @Id
    private long address_id;

    @Column(name="address")
    private String address;

    @Column(name="city")
    private String city;

    @Column(name="province")
    private String province;

    @Column(name="postal_code")
    private String postalCode;

    @OneToOne
    @JoinColumn(name="servicearea_id", referencedColumnName = "servicearea_id")
    private ServiceArea servicearea;

}