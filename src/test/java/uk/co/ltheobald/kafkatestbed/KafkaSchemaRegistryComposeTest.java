package uk.co.ltheobald.kafkatestbed;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class KafkaSchemaRegistryComposeTest extends KafkaSchemaRegistryTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSchemaRegistryComposeTest.class);

    @Test
    void testKafkaAndSchemaRegistryAreRunning() {
        String kafkaHost = environment.getServiceHost("kafka", 29092);
        Integer kafkaPort = environment.getServicePort("kafka", 29092);

        String schemaRegistryHost = environment.getServiceHost("schema-registry", 8085);
        Integer schemaRegistryPort = environment.getServicePort("schema-registry", 8085);

        LOGGER.info("Kafka: {}:{}", kafkaHost, kafkaPort);
        LOGGER.info("Schema Registry: http://{}:{}", schemaRegistryHost, schemaRegistryPort);

        assertTrue(kafkaPort > 0);
        assertTrue(schemaRegistryPort > 0);
    }
}