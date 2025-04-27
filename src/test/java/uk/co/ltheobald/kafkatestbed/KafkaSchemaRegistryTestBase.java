package uk.co.ltheobald.kafkatestbed;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;

public class KafkaSchemaRegistryTestBase {

    static final DockerComposeContainer<?> environment =
            new DockerComposeContainer<>(new File("docker-compose.yaml"))
                    .withExposedService("kafka", 29092, Wait.forListeningPort())
                    .withExposedService("schema-registry", 8085, Wait.forHttp("/subjects").forStatusCode(200));

    @BeforeAll
    static void setUp() {
        environment.start();
    }

    @AfterAll
    static void tearDown() {
        //environment.stop();
    }
}