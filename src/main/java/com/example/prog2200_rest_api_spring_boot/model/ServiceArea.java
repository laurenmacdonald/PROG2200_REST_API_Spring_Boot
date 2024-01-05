package com.example.prog2200_rest_api_spring_boot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/* Creating entity, connecting to table named servicearea in alertsystem MySQL database. OneToOne relationship with firestation table.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="servicearea")
public class ServiceArea {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long servicearea_id;

    @Column(name="service_area")
    private int service_area;
    @OneToOne
    @JoinColumn(name="station_id", referencedColumnName = "station_id")
    private FireStation fireStation;


}
