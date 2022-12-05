package org.example.db;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.util.Collections;
import java.util.Map;

public class PostgresResource implements QuarkusTestResourceLifecycleManager {

    static TestPostgresContainer container = TestPostgresContainer.getInstance();

    @Override
    public Map<String, String> start() {
        container.start();
        System.out.println("Container address is: " + container.getHost() + ":" + container.getMappedPort(5432));
        return Collections.singletonMap("quarkus.datasource.url", container.getJdbcUrl());
    }

    @Override
    public void stop() {
        container.stop();
    }
}
