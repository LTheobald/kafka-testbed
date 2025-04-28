package uk.co.ltheobald.kafkatestbed.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.co.ltheobald.kafkatestbed.FraudResult;
import uk.co.ltheobald.kafkatestbed.Transaction;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

class FraudServiceTest {

  private FraudService fraudService = new FraudService();

  /**
   * Create two transactions, one which we know will flag as fraud and test them
   */
  @Test
  void checkTransactionForFraud() {
    Transaction nonFraudTx = Transaction.newBuilder()
            .setAmount(10.00)
            .setCurrency("GBP")
            .setMerchantId("merchant-1")
            .setTransactionId(randomUUID())
            .setTimestamp(now())
            .setStatementNarrative("Test transaction")
            .build();
    FraudResult nonFraudTxResult = fraudService.checkTransactionForFraud(nonFraudTx);

    Transaction fraudTx = Transaction.newBuilder()
            .setAmount(1.99) // An amount .99 being what we deem as fraud in this demo
            .setCurrency("GBP")
            .setMerchantId("merchant-1")
            .setTransactionId(randomUUID())
            .setTimestamp(now())
            .setStatementNarrative("Test transaction")
            .build();
    FraudResult fraudTxResult = fraudService.checkTransactionForFraud(fraudTx);

    assertNotNull(nonFraudTx);
    assertNotNull(nonFraudTxResult);
    assertFalse(nonFraudTxResult.getFraudDetected());

    assertNotNull(fraudTx);
    assertNotNull(fraudTxResult);
    assertTrue(fraudTxResult.getFraudDetected());
  }
}