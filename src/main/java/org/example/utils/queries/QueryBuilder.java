package org.example.utils.queries;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import org.example.utils.statistics.Statistics;

public class QueryBuilder {

    private static final String SPACE = " ";
    private static final String COMMA = ",";
    private static final String DOT = ".";
    private static final String SINGLE_QUOTE = "'";
    private static final String OPEN_PARENTHESIS = "(";
    private static final String CLOSED_PARENTHESIS = ")";

    public static String buildQuery(List<String> sensors, List<String> metrics, Statistics statistic, LocalDate startDate, LocalDate endDate) {
        StringBuilder query = new StringBuilder();
        buildSelect(query, metrics, statistic.getStatistic());
        buildFrom(query);
        buildWhere(query, startDate, endDate, sensors);

        return query.toString();
    }

    private static void buildSelect(StringBuilder select, List<String> metrics, String aggFunction) {
        // Want this in the format
        // SELECT MIN(w.temperature), MIN(w.humidity)
        select.append("SELECT");
        select.append(SPACE);

        Iterator<String> it = metrics.iterator();
        while (it.hasNext()) {
            select.append(aggFunction);
            select.append(OPEN_PARENTHESIS);
            String metric = it.next();
            select.append("w");
            select.append(DOT);
            select.append(metric);
            select.append(CLOSED_PARENTHESIS);

            if (it.hasNext()) {
                select.append(COMMA);
                select.append(SPACE);
            }
        }

        select.append(SPACE);
    }

    // Probably overkill
    private static void buildFrom(StringBuilder from) {
        from.append("FROM");
        from.append(SPACE);
        from.append("WeatherData");
        from.append(SPACE);
        from.append("w");
        from.append(SPACE);
    }

    private static void buildWhere(StringBuilder where, LocalDate startDate, LocalDate endDate, List<String> sensorIds) {
        where.append("WHERE");
        where.append(SPACE);
        filterDates(where, startDate, endDate);

        if (sensorIds != null && sensorIds.size() > 0) {
            filterSensors(where, sensorIds);
        }
    }

    private static void filterDates(StringBuilder where, LocalDate startDate, LocalDate endDate) {
        // This is in the format
        // w.dateTimestamp <= '2022-12-01' AND w.dateTimestamp >= '2022-11-20'
        where.append("w.dateTimestamp");
        where.append(SPACE);
        where.append("<=");
        where.append(SPACE);
        where.append(SINGLE_QUOTE);
        where.append(startDate.toString());
        where.append(SINGLE_QUOTE);
        where.append(SPACE);
        where.append("AND");
        where.append(SPACE);
        where.append("w.dateTimestamp");
        where.append(SPACE);
        where.append(">=");
        where.append(SPACE);
        where.append(SINGLE_QUOTE);
        where.append(endDate.toString());
        where.append(SINGLE_QUOTE);
    }

    private static void filterSensors(StringBuilder where, List<String> sensorIds) {
        // This is in the format
        // AND w.sensor IN ('sensor_1', 'sensor_2')
        where.append(SPACE);
        where.append("AND");
        where.append(SPACE);
        where.append("w.sensor");
        where.append(SPACE);
        where.append("IN");
        where.append(SPACE);
        where.append(OPEN_PARENTHESIS);
        Iterator<String> it = sensorIds.iterator();
        while (it.hasNext()) {
            String sensor = it.next();
            where.append(SINGLE_QUOTE);
            where.append(sensor);
            where.append(SINGLE_QUOTE);
            if (it.hasNext()) {
                where.append(COMMA);
                where.append(SPACE);
            }
        }
        where.append(CLOSED_PARENTHESIS);
    }
}
