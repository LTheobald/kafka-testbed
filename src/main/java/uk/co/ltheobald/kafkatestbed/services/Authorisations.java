package uk.co.ltheobald.kafkatestbed.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uk.co.ltheobald.kafkatestbed.Transaction;

import java.time.Instant;
import java.util.UUID;
import java.util.random.RandomGenerator;

@Service
class Authorisations {
    private static final double MIN_AMOUNT = 0.01;
    private static final double MAX_AMOUNT = 150.00;
    private static final RandomGenerator RANDOM_GENERATOR = RandomGenerator.of("L64X256MixRandom");


    /**
     * Create a random transaction every X seconds
     */
    @Scheduled(fixedRate = 2000)
    public void createAuthorisations() {
        Transaction tx = Transaction.newBuilder()
                .setAmount(MIN_AMOUNT + RANDOM_GENERATOR.nextDouble() * (MAX_AMOUNT - MIN_AMOUNT))
                .setCurrency("GBP")
                .setMerchantId("merchant-1")
                .setTransactionId(UUID.randomUUID())
                .setTimestamp(Instant.now())
                .build();
    }
}