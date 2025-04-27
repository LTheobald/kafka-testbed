package uk.co.ltheobald.kafkatestbed.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;
import uk.co.ltheobald.kafkatestbed.FraudResult;
import uk.co.ltheobald.kafkatestbed.KafkaSchemaRegistryTestBase;
import uk.co.ltheobald.kafkatestbed.Transaction;
import uk.co.ltheobald.kafkatestbed.listeners.Authorisations;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class AuthorisationsTest extends KafkaSchemaRegistryTestBase {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private Authorisations authorisations;

    private final String AUTH_TOPIC = "authorisations";
    private final String FRAUD_TOPIC = "fraud";

    @Test
    void shouldStoreTransactionOnAuthorisationsTopic() throws Exception {
        UUID transactionId = UUID.randomUUID();
        Transaction tx = Transaction.newBuilder()
                .setAmount(100.00)
                .setCurrency("GBP")
                .setMerchantId("merchant-1")
                .setTransactionId(transactionId)
                .setTimestamp(Instant.now())
                .setStatementNarrative("Test transaction")
                .build();

        kafkaTemplate.send(AUTH_TOPIC, tx);

        // Allow some time for the listener to consume the message
        Thread.sleep(1000);

        // Access internal state for test (you may expose this or use reflection if needed)
        Transaction stored = getPendingTransactionFromAuthorisations(authorisations, transactionId);
        assertNotNull(stored);
        assertEquals(tx.getAmount(), stored.getAmount());
    }

    @Test
    void shouldLogApprovedTransaction() throws Exception {
        UUID transactionId = UUID.randomUUID();
        Transaction tx  = Transaction.newBuilder()
                .setAmount(100.00)
                .setCurrency("GBP")
                .setMerchantId("merchant-1")
                .setTransactionId(transactionId)
                .setTimestamp(Instant.now())
                .setStatementNarrative("Test transaction")
                .build();
        kafkaTemplate.send(AUTH_TOPIC, tx);

        Thread.sleep(500); // wait for processing
        FraudResult result = FraudResult.newBuilder()
                .setTimestamp(Instant.now())
                .setFraudDetected(false)
                .setTransactionId(transactionId)
                .setFraudResultId(UUID.randomUUID())
                .build();
        kafkaTemplate.send(FRAUD_TOPIC, result);

        Thread.sleep(1000); // wait for processing

        // Validate logs using a log appender or ensure transaction is removed from pending
        Transaction removed = getPendingTransactionFromAuthorisations(authorisations, transactionId);
        assertNull(removed);
    }

    private Transaction getPendingTransactionFromAuthorisations(Authorisations auths, UUID txId) throws Exception {
        // Access the private pending map via reflection
        Field pendingField = Authorisations.class.getDeclaredField("pending");
        pendingField.setAccessible(true);
        Map<UUID, Transaction> pendingMap = (Map<UUID, Transaction>) pendingField.get(auths);
        return pendingMap.get(txId);
    }
}