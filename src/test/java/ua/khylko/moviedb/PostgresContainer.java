package ua.khylko.moviedb;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;

@DirtiesContext
@ActiveProfiles("test")
public abstract class PostgresContainer {
    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
            .withExposedPorts(5432)
            .withDatabaseName("moviedb")
            .withUsername("postgres")
            .withPassword("spider1998")
            .withInitScript("moviedb_test.pgsql")
            .waitingFor(Wait.forListeningPort());

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", postgres::getJdbcUrl);
        propertyRegistry.add("spring.datasource.password", postgres::getPassword);
        propertyRegistry.add("spring.datasource.username", postgres::getUsername);
    }
}
