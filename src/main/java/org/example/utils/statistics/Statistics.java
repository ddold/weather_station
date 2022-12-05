package org.example.utils.statistics;

public enum Statistics {
    AVERAGE("AVG"),
    MIN("MIN"),
    MAX("MAX"),
    SUM("SUM");

    private String statistic;

    Statistics(String statistic) {
        this.statistic = statistic;
    }

    public String getStatistic() {
        return statistic;
    }
}
