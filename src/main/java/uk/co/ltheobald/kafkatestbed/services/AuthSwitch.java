package uk.co.ltheobald.kafkatestbed.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uk.co.ltheobald.kafkatestbed.Transaction;

import java.time.Instant;
import java.util.UUID;
import java.util.random.RandomGenerator;

@Component
class AuthSwitch {
    private static Logger LOGGER = LoggerFactory.getLogger(AuthSwitch.class);
    public static final String TOPIC = "authorisations";


    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final double MIN_AMOUNT = 0.01;
    private static final double MAX_AMOUNT = 150.00;
    private static final RandomGenerator RANDOM_GENERATOR = RandomGenerator.of("L64X256MixRandom");

    @Autowired
    public AuthSwitch(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Create a random transaction every X seconds
     */
    @Scheduled(fixedRate = 5000)
    public void createAuthorisations() {
        Transaction tx = Transaction.newBuilder()
                .setAmount(MIN_AMOUNT + RANDOM_GENERATOR.nextDouble() * (MAX_AMOUNT - MIN_AMOUNT))
                .setCurrency("GBP")
                .setMerchantId("merchant-1")
                .setTransactionId(UUID.randomUUID())
                .setTimestamp(Instant.now())
                .setStatementNarrative("Test transaction")
                .build();

        LOGGER.debug("Created transaction with ID {}", tx.getTransactionId().toString());
        kafkaTemplate.send(TOPIC, tx.getTransactionId().toString(), tx);
    }
}