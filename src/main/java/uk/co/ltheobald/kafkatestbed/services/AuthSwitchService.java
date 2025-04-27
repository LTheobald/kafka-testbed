package uk.co.ltheobald.kafkatestbed.services;

import static java.time.Instant.*;
import static java.util.UUID.*;

import java.util.random.RandomGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uk.co.ltheobald.kafkatestbed.Transaction;

/** Mocks a new transaction coming into the switch. Creates random transactions instead. */
@Service
public class AuthSwitchService {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthSwitchService.class);

  private static final double MIN_AMOUNT = 0.01;
  private static final double MAX_AMOUNT = 150.00;
  private static final RandomGenerator RANDOM_GENERATOR = RandomGenerator.of("L64X256MixRandom");

  /**
   * Creates a random transaction that can be added to a Kafka topic
   *
   * @return A new transaction
   */
  public Transaction createIncomingTransaction() {
    Transaction tx =
        Transaction.newBuilder()
            .setAmount(MIN_AMOUNT + RANDOM_GENERATOR.nextDouble() * (MAX_AMOUNT - MIN_AMOUNT))
            .setCurrency("GBP")
            .setMerchantId("merchant-1")
            .setTransactionId(randomUUID())
            .setTimestamp(now())
            .setStatementNarrative("Test transaction")
            .build();

    LOGGER.debug("Created transaction with ID {}", tx.getTransactionId());
    return tx;
  }
}
