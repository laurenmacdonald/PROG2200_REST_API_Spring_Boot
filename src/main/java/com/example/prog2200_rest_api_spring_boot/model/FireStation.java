package com.example.prog2200_rest_api_spring_boot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* Creating entity, connecting to table named firestation in alertsystem MySQL database. OneToOne relationship used between
   this class and the ServiceArea class
*/
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="firestation")
public class FireStation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long station_id;

    @Column(name="station_number")
    private int stationNumber;

    @OneToOne
    @JoinColumn(name="servicearea_id", referencedColumnName = "servicearea_id")
    private ServiceArea servicearea;

}
