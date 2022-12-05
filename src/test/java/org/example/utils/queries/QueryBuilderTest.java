package org.example.utils.queries;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.example.utils.statistics.Statistics;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class QueryBuilderTest {

    @Test
    void test_simpleQuery() {
        List<String> metrics = new ArrayList<>();
        metrics.add("a");
        metrics.add("b");

        String expectedQuery = "SELECT AVG(w.a), AVG(w.b) FROM WeatherData w WHERE w.dateTimestamp <= '2022-12-03' AND w.dateTimestamp >= '2022-12-02'";
        String testQuery = QueryBuilder.buildQuery(null, metrics, Statistics.AVERAGE, LocalDate.of(2022, 12, 3), LocalDate.of(2022, 12, 2));

        assertEquals(expectedQuery, testQuery);
    }

    @Test
    void test_queryWithMoreParams() {
        List<String> metrics = new ArrayList<>();
        metrics.add("a");
        metrics.add("b");

        List<String> sensors = new ArrayList<>();
        sensors.add("c");
        sensors.add("d");

        String expectedQuery = "SELECT MAX(w.a), MAX(w.b) FROM WeatherData w WHERE w.dateTimestamp <= '2022-12-03' AND w.dateTimestamp >= '2022-12-02' AND w.sensor IN ('c', 'd')";
        String testQuery = QueryBuilder.buildQuery(sensors, metrics, Statistics.MAX, LocalDate.of(2022, 12, 3), LocalDate.of(2022, 12, 2));

        assertEquals(expectedQuery, testQuery);
    }
}
