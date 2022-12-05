package org.example.controller;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.example.controllers.WeatherDataController;
import org.example.db.PostgresResource;
import org.example.entities.WeatherData;
import org.example.repositories.WeatherDataRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@Transactional
@TestHTTPEndpoint(WeatherDataController.class)
@QuarkusTestResource(PostgresResource.class)
public class WeatherDataControllerTest {

    @Inject
    WeatherDataRepository repo;

    @AfterEach
    void cleanup() {
        repo.deleteAll();
    }

    @Test
    void test_post() {
        WeatherData data = given()
                .contentType(ContentType.JSON)
                .body(createWeatherData())
                    .post()
                .then()
                    .statusCode(201)
                    .extract()
                        .as(WeatherData.class);

        assertThat(data.getId()).isNotNull();
    }

    @Test
    void test_get() {
        // Save date
        given()
                .contentType(ContentType.JSON)
                .body(createWeatherData())
                    .post()
                .then()
                    .statusCode(201);

        WeatherData data = given()
                .contentType(ContentType.JSON)
                .queryParam("metric", "temperature")
                .queryParam("metric", "humidity")
                .queryParam("statistic", "max")
                .get()
                .then()
                    .statusCode(200)
                    .extract()
                        .as(WeatherData.class);

        assertEquals(data.getHumidity(), 3.4);
    }

    private WeatherData createWeatherData() {
        WeatherData data = new WeatherData();
        data.setDateTimestamp(LocalDate.now());
        data.setHumidity(3.4);
        data.setSensor("sensor_1");
        data.setTemperature(5.6);
        data.setWindSpeed(7.9);
        return data;
    }

}
