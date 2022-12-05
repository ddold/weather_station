package org.example.entities;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "WeatherData")
@Table(name = "weatherdata")
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private double temperature;

    private double humidity;

    @Column(name = "wind_speed")
    private double windSpeed;

    @NotEmpty(message = "Weather data needs to be linked to a sensor")
    private String sensor;

    @Column(name = "date_timestamp", nullable = false, updatable = false)
    private LocalDate dateTimestamp;

}
