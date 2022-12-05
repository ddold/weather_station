package org.example.db;

import org.testcontainers.containers.PostgreSQLContainer;

public class TestPostgresContainer extends PostgreSQLContainer<TestPostgresContainer> {
    private static final String IMAGE_VERSION = "postgres:13.3-alpine";
    private static TestPostgresContainer container;

    private TestPostgresContainer() {
        super(IMAGE_VERSION);
    }

    public static TestPostgresContainer getInstance() {
        if (container == null) {
            container = new TestPostgresContainer().withExposedPorts(5432);
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USER", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
