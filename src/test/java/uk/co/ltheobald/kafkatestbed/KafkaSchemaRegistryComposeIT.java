package uk.co.ltheobald.kafkatestbed;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.Duration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("integration")
@Testcontainers
public class KafkaSchemaRegistryComposeIT {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(KafkaSchemaRegistryComposeIT.class);

  static final DockerComposeContainer<?> environment =
      new DockerComposeContainer<>(new File("docker-compose.yaml"))
          .withExposedService("postgres", 5432, Wait.forListeningPort())
          .withExposedService("kafka", 29092, Wait.forListeningPort())
          .withExposedService(
              "schema-registry",
              8085,
              Wait.forHttp("/subjects")
                  .forStatusCode(200)
                  .withStartupTimeout(Duration.ofMinutes(2)))
          .withStartupTimeout(Duration.ofMinutes(3));

  @BeforeAll
  static void setUp() {
    environment.start();
  }

  @AfterAll
  static void tearDown() {
    // Removing for now. The integration test is running early so the below line causes spring
    // issues
    // environment.stop();
  }

  @Test
  void testKafkaAndSchemaRegistryAreRunning() {
    String kafkaHost = environment.getServiceHost("kafka", 29092);
    Integer kafkaPort = environment.getServicePort("kafka", 29092);

    String schemaRegistryHost = environment.getServiceHost("schema-registry", 8085);
    Integer schemaRegistryPort = environment.getServicePort("schema-registry", 8085);

    LOGGER.info("Kafka: {}:{}", kafkaHost, kafkaPort);
    LOGGER.info("Schema Registry: http://{}:{}", schemaRegistryHost, schemaRegistryPort);

    assertFalse(kafkaHost.isEmpty());
    assertFalse(schemaRegistryHost.isEmpty());
    assertTrue(kafkaPort > 0);
    assertTrue(schemaRegistryPort > 0);
  }
}
