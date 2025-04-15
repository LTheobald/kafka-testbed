package uk.co.ltheobald.kafkatestbed.services;

import java.util.Properties;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// @SpringBootTest
class AuthSwitchTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthSwitchTest.class);

    // @Autowired
    private AuthSwitch authSwitch;

    // @BeforeAll
    public static void kafkaConnectionCheck() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");

        try (AdminClient adminClient = AdminClient.create(props)) {
            LOGGER.info("Kafka is running. Topics found are {}", adminClient.listTopics().names().get()); // Will throw if Kafka is unreachable
            assert true;
        } catch (Exception e) {
            throw new RuntimeException("Kafka is not running, aborting tests", e);
        }
    }

    // @Test
    void createAuthorisation() {
        authSwitch.createAuthorisations();
    }
}