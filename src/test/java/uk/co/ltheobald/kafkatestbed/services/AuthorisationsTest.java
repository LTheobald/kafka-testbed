package uk.co.ltheobald.kafkatestbed.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
class AuthorisationsTest {

    private static final Network NETWORK = Network.newNetwork();

    @Container
    private static final KafkaContainer KAFKA_CONTAINER =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.2"))
                    .withNetwork(NETWORK);

    @Container
    private static final GenericContainer<?> SCHEMA_REGISTRY =
            new GenericContainer<>(DockerImageName.parse("confluentinc/cp-schema-registry:7.5.2"))
                    .withNetwork(NETWORK)
                    .withExposedPorts(8081)
                    .withEnv("SCHEMA_REGISTRY_HOST_NAME", "schema-registry")
                    .withEnv("SCHEMA_REGISTRY_LISTENERS", "http://0.0.0.0:8081")
                    .withEnv("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS",
                            "PLAINTEXT://" + KAFKA_CONTAINER.getNetworkAliases().get(0) + ":9092")
                    .waitingFor(Wait.forHttp("/subjects").forStatusCode(200));

    @Autowired
    private Authorisations authorisations;

    void createAuthorisation() {
        String topicName = "notifications-test";
        String bootstrapServers = KAFKA_CONTAINER.getBootstrapServers();


    }
}