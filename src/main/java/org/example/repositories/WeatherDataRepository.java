package org.example.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import org.example.entities.WeatherData;

@ApplicationScoped
public class WeatherDataRepository implements PanacheRepository<WeatherData> {

    @Transactional
    public WeatherData persistWeatherData(WeatherData weatherData) {
        persist(weatherData);
        return weatherData;
    }
}
