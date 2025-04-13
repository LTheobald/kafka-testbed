package uk.co.ltheobald.kafkatestbed;

import org.slf4j.LoggerFactory;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class TestContainerConfiguration {

  private static final Network NETWORK = Network.newNetwork();

  @Bean
  public GenericContainer<?> kafkaContainer() {
    return new GenericContainer<>(DockerImageName.parse("confluentinc/cp-kafka:7.9.0"))
        .withNetworkAliases("kafka")
        .withExposedPorts(9092)
        .withEnv("KAFKA_KRAFT_MODE", "true")
        .withEnv("KAFKA_PROCESS_ROLES", "broker,controller")
        .withEnv("KAFKA_CONTROLLER_QUORUM_VOTERS", "1@kafka:9093")
        .withEnv("KAFKA_NODE_ID", "1")
        .withEnv("KAFKA_LISTENERS", "PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093")
        .withEnv("KAFKA_ADVERTISED_LISTENERS", "PLAINTEXT://kafka:9092")
        .withEnv("KAFKA_LISTENER_SECURITY_PROTOCOL_MAP", "PLAINTEXT:PLAINTEXT,CONTROLLER:PLAINTEXT")
        .withEnv("KAFKA_INTER_BROKER_LISTENER_NAME", "PLAINTEXT")
        .withEnv("KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR", "1")
        .withEnv("KAFKA_TRANSACTION_STATE_LOG_MIN_ISR", "1")
        .withEnv("KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR", "1")
        .withEnv("KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS", "0")
        .withEnv("KAFKA_CONTROLLER_LISTENER_NAMES", "CONTROLLER")
        .withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "true")
        .withEnv("KAFKA_CLUSTER_ID", "my-test-cluster")
        .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("KafkaContainer")));
  }

  /**
   * @Bean @ServiceConnection public GenericContainer<?>
   * schemaRegistryContainer(ConfluentKafkaContainer kafkaContainer) { return new
   * GenericContainer<>("confluentinc/cp-schema-registry:7.9.0") .withNetwork(NETWORK)
   * .withNetworkAliases("schemaregistry") .withEnv("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS",
   * "BROKER://kafka:9092") .withEnv("SCHEMA_REGISTRY_HOST_NAME", "schemaregistry")
   * .withEnv("SCHEMA_REGISTRY_LISTENERS", "http://0.0.0.0:8085") .dependsOn(kafkaContainer)
   * .withExposedPorts(8085); } @DynamicPropertySource static void
   * registerDynamicProperties(GenericContainer<?> schemaRegistryContainer, DynamicPropertyRegistry
   * registry) { registry.add("spring.kafka.consumer.properties.schema.registry.url", () ->
   * "http://" + schemaRegistryContainer.getHost() + ":" +
   * schemaRegistryContainer.getMappedPort(8085)); }
   */
}
