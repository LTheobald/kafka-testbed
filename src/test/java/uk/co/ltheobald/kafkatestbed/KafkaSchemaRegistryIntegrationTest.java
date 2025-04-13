package uk.co.ltheobald.kafkatestbed;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

@SpringBootTest
@Testcontainers
public class KafkaSchemaRegistryIntegrationTest {

  @Autowired
  private GenericContainer kafkaContainer;

  @Autowired
  private GenericContainer<?> schemaRegistryContainer;

  @Test
  void testContainersAreRunning() throws ExecutionException, InterruptedException {
    // Verify that the Kafka container is running
    assertNotNull(kafkaContainer);
    assertNotNull(kafkaContainer.getBootstrapServers());

    // Verify that the Schema Registry container is running
    assertNotNull(schemaRegistryContainer);
    assertNotNull(schemaRegistryContainer.getMappedPort(8085));

    // Example: Connect to Kafka using AdminClient
    Properties props = new Properties();
    props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
    try (AdminClient adminClient = AdminClient.create(props)) {
      assertNotNull(adminClient.listTopics().names().get());
    }
  }
}