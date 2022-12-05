package org.example.services;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.example.entities.WeatherData;
import org.example.exceptions.QueryException;
import org.example.repositories.WeatherDataRepository;
import org.example.utils.queries.QueryBuilder;
import org.example.utils.statistics.Statistics;

@ApplicationScoped
public class WeatherDataService {

    @Inject
    WeatherDataRepository repo;

    public WeatherData saveWeatherData(WeatherData data) {
        if (data.getDateTimestamp() == null) {
            data.setDateTimestamp(LocalDate.now());
        }

        return repo.persistWeatherData(data);
    }

    public Map<String, Object> getWeatherData(List<String> sensorIds, List<String> metrics, String statistic, String dateStart, String dateEnd)
            throws QueryException {

        // Get and check the statistic
        Statistics stat = Statistics.AVERAGE;
        if (statistic != null) {
            try {
                stat = Statistics.valueOf(statistic.toUpperCase());
            } catch (Exception e) {
                throw new QueryException("There is no statistic " + statistic + ".", e);
            }
        }

        // Get and check the dates
        LocalDate startDate = getStartDate(dateStart);
        LocalDate endDate = getEndDate(dateEnd, startDate);
        validateDates(startDate, endDate);

        // Check the metrics
        if (metrics == null || metrics.size() == 0) {
            throw new QueryException("Need to include what metrics to query");
        }

        String query = QueryBuilder.buildQuery(sensorIds, metrics, stat, startDate, endDate);

        // It's only ever the first result as we will be using an ag function in the query
        Object data = repo.find(query).firstResult();

        return createResponse(data, metrics);
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) throws QueryException {
        // What breaks date validation...?
        // 1. start date in the future
        // 2. end date greater than start date
        // 3. end date more than 30 days
        if (startDate.isAfter(LocalDate.now())) {
            throw new QueryException("Start date can't be in the future");
        }
        if (endDate.isAfter(startDate)) {
            throw new QueryException("End date can't be after the start date");
        }
        if (ChronoUnit.DAYS.between(endDate, startDate) > 30) {
            throw new QueryException("Can only query within the last 30 days");
        }
    }

    private LocalDate getEndDate(String dateEnd, LocalDate startDate) {
        if (dateEnd == null) {
            return startDate.minus(Period.ofDays(30));
        }
        return LocalDate.parse(dateEnd);
    }

    private LocalDate getStartDate(String dateStart) {
        if (dateStart == null) {
            return LocalDate.now();
        }
        return LocalDate.parse(dateStart);
    }

    private Map<String, Object> createResponse(Object data, List<String> metrics) {
        Map<String, Object> response = new HashMap<>();
        if (metrics.size() == 1) {
            response.put(metrics.get(0), data);
            return response;
        }

        for (int i = 0; i < metrics.size(); i++) {
            response.put(metrics.get(i), ((Object[]) data)[i]);
        }

        return response;
    }

}
