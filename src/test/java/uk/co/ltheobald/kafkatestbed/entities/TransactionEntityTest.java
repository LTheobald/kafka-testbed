package uk.co.ltheobald.kafkatestbed.entities;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import uk.co.ltheobald.kafkatestbed.Transaction;

@SpringBootTest
public class TransactionEntityTest {

    /**
     * Ensure the constructor correct converts between the two Transaction objects
     */
    @Test
    void transactionConversionTest() {
        // Create a Transaction object
        Transaction transaction = new Transaction();
        transaction.setTransactionId(randomUUID());
        transaction.setTimestamp(now());
        transaction.setMerchantId("merchant123");
        transaction.setAmount(100.0);
        transaction.setCurrency("USD");
        transaction.setStatementNarrative("Test transaction");

        // Convert to TransactionEntity
        TransactionEntity entity = new TransactionEntity(transaction);

        // Assertions
        assertNotNull(entity);
        assertEquals(transaction.getTransactionId(), entity.getTransactionId());
        assertEquals(Timestamp.from(transaction.getTimestamp()), entity.getTimestamp());
        assertEquals(transaction.getMerchantId().toString(), entity.getMerchantId());
        assertEquals(transaction.getAmount(), entity.getAmount());
        assertEquals(transaction.getCurrency().toString(), entity.getCurrency());
        assertEquals(transaction.getStatementNarrative().toString(), entity.getStatementNarrative());
    }
}
