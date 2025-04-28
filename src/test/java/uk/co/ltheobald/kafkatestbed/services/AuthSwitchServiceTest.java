package uk.co.ltheobald.kafkatestbed.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.co.ltheobald.kafkatestbed.Transaction;

@SpringBootTest
class AuthSwitchServiceTest {

  @Autowired private AuthSwitchService authSwitchService;

  /** Create a number of transactions & ensure none of them are identical */
  @Test
  void createIncomingTransaction() {
    Transaction tx1 = authSwitchService.createIncomingTransaction();
    Transaction tx2 = authSwitchService.createIncomingTransaction();
    Transaction tx3 = authSwitchService.createIncomingTransaction();
    Transaction tx4 = authSwitchService.createIncomingTransaction();
    Transaction tx5 = authSwitchService.createIncomingTransaction();

    assertNotNull(tx1);
    assertNotNull(tx2);
    assertNotNull(tx3);
    assertNotNull(tx4);
    assertNotNull(tx5);

    // Using a Set to make sure all objects are using (via their equals/hashcode). Also specifically
    // check the IDs.
    assertEquals(5, Set.of(tx1, tx2, tx3, tx4, tx5).size());
    assertEquals(
        5,
        Set.of(
                tx1.getTransactionId(),
                tx2.getTransactionId(),
                tx3.getTransactionId(),
                tx4.getTransactionId(),
                tx5.getTransactionId())
            .size());
  }
}
