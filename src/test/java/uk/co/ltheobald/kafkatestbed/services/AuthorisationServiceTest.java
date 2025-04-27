package uk.co.ltheobald.kafkatestbed.services;

import static java.sql.Timestamp.*;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import uk.co.ltheobald.kafkatestbed.FraudResult;
import uk.co.ltheobald.kafkatestbed.Transaction;
import uk.co.ltheobald.kafkatestbed.entities.TransactionEntity;
import uk.co.ltheobald.kafkatestbed.repositories.TransactionRepository;

@SpringJUnitConfig
class AuthorisationServiceTest {

  @InjectMocks private AuthorisationService authorisationService;

  @Mock private TransactionRepository transactionRepository;

  @Test
  void addPendingTransaction_ShouldSaveTransaction() {
    Transaction transaction =
        new Transaction(now(), randomUUID(), "merchant-1", 10.00, "GBP", "Test transaction");
    TransactionEntity shouldMatchEntity = new TransactionEntity(transaction);

    authorisationService.addPendingTransaction(transaction);

    verify(transactionRepository).save(shouldMatchEntity);
  }

  @Test
  void processAcceptedFraudResult() {
    UUID transactionId = randomUUID();
    TransactionEntity acceptedTransaction =
        new TransactionEntity(
            transactionId, from(now()), "merchant-1", 10.00, "GBP", "Test transaction");

    FraudResult acceptedFraudResult =
        FraudResult.newBuilder()
            .setTimestamp(now())
            .setFraudDetected(false)
            .setTransactionId(transactionId)
            .setFraudResultId(randomUUID())
            .build();

    when(transactionRepository.findById(transactionId))
        .thenReturn(Optional.of(acceptedTransaction));

    AuthorisationOutcome outcome = authorisationService.processFraudResult(acceptedFraudResult);

    assertEquals(AuthorisationOutcome.APPROVED, outcome);
  }

  @Test
  void processDeclinedFraudResult() {
    UUID transactionId = randomUUID();
    TransactionEntity declinedTransaction =
            new TransactionEntity(
                    transactionId, from(now()), "merchant-1", 1.99, "GBP", "Test transaction");

    FraudResult declinedFraudResult =
            FraudResult.newBuilder()
                    .setTimestamp(now())
                    .setFraudDetected(true)
                    .setTransactionId(transactionId)
                    .setFraudResultId(randomUUID())
                    .build();

    when(transactionRepository.findById(transactionId))
            .thenReturn(Optional.of(declinedTransaction));

    AuthorisationOutcome outcome = authorisationService.processFraudResult(declinedFraudResult);

    assertEquals(AuthorisationOutcome.DECLINED, outcome);
  }

  @Test
  void processNotFoundFraudResult() {
    UUID transactionId = randomUUID();
    FraudResult declinedFraudResult =
            FraudResult.newBuilder()
                    .setTimestamp(now())
                    .setFraudDetected(true)
                    .setTransactionId(transactionId)
                    .setFraudResultId(randomUUID())
                    .build();

    when(transactionRepository.findById(transactionId))
            .thenThrow(EntityNotFoundException.class);

    AuthorisationOutcome outcome = authorisationService.processFraudResult(declinedFraudResult);

    assertEquals(AuthorisationOutcome.NOT_FOUND, outcome);
  }
}
