DROP TABLE IF EXISTS weatherdata;

CREATE TABLE IF NOT EXISTS weatherdata (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    temperature NUMERIC(4, 2),
    humidity NUMERIC(4, 2),
    wind_speed NUMERIC(4, 2),
    sensor VARCHAR NOT NULL,
    date_timestamp DATE DEFAULT CURRENT_DATE,
    PRIMARY KEY(id)
);