package uk.co.ltheobald.kafkatestbed.services;

import java.time.Instant;
import java.util.UUID;
import java.util.random.RandomGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uk.co.ltheobald.kafkatestbed.FraudResult;
import uk.co.ltheobald.kafkatestbed.Transaction;

/** Mocks a new transaction coming into the switch. Creates random transactions instead. */
@Service
public class FraudService {
  private static final Logger LOGGER = LoggerFactory.getLogger(FraudService.class);

  private static final double MIN_AMOUNT = 0.01;
  private static final double MAX_AMOUNT = 150.00;
  private static final RandomGenerator RANDOM_GENERATOR = RandomGenerator.of("L64X256MixRandom");

  /**
   * Checks a transaction for fraud.
   *
   * @param tx Transaction to check for fraud
   * @return A FraudResult object containing the details of the fraud check
   */
  public FraudResult checkTransactionForFraud(Transaction tx) {
    FraudResult fraudResult =
        FraudResult.newBuilder()
            .setTimestamp(Instant.now())
            .setFraudDetected(false)
            .setTransactionId(tx.getTransactionId())
            .setFraudResultId(UUID.randomUUID())
            .build();

    // If an amount ends in .99, we'll use that to act as if something is fraud
    if (tx.getAmount() % 1 == 0.99) {
      LOGGER.info(
          "Fraud detected for transaction ID: {} with amount: {}",
          tx.getTransactionId(),
          tx.getAmount());
      fraudResult.setFraudDetected(true);
    }

    return fraudResult;
  }
}
