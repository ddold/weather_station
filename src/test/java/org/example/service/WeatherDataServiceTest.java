package org.example.service;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.example.db.PostgresResource;
import org.example.entities.WeatherData;
import org.example.exceptions.QueryException;
import org.example.repositories.WeatherDataRepository;
import org.example.services.WeatherDataService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
@Transactional
@QuarkusTestResource(PostgresResource.class)
public class WeatherDataServiceTest {

    @Inject
    WeatherDataService service;

    @Inject
    WeatherDataRepository repo;

    @AfterEach
    void cleanup() {
        repo.deleteAll();
    }

    @Test
    void test_saveWeatherData() {
        WeatherData data = createData();

        WeatherData savedData = service.saveWeatherData(data);
        WeatherData gotData = repo.findById(savedData.getId());
        assertEquals(savedData, gotData);
    }

    @Test
    void test_getWeatherData() throws QueryException {
        WeatherData data = createData();
        service.saveWeatherData(data);

        List<String> metrics = new ArrayList<>();
        metrics.add("temperature");
        metrics.add("humidity");
        Map<String, Object> queryResult = service.getWeatherData(null, metrics, "min", null, null);
        assertEquals(queryResult.get("temperature"), 5.6);
    }

    @Test
    void test_failOnDate() {
        assertThrows(QueryException.class, () -> service.getWeatherData(null, null, null, LocalDate.MAX.toString(), null));
        assertThrows(QueryException.class, () -> service.getWeatherData(null, null, null, LocalDate.now().toString(), LocalDate.MAX.toString()));
        assertThrows(QueryException.class, () -> service.getWeatherData(null, null, null, LocalDate.MAX.toString(), LocalDate.MIN.toString()));
    }

    private WeatherData createData() {
        WeatherData data = new WeatherData();
        data.setDateTimestamp(LocalDate.now());
        data.setHumidity(3.4);
        data.setSensor("sensor_1");
        data.setTemperature(5.6);
        data.setWindSpeed(7.9);
        return data;
    }

}
