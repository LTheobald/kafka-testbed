package uk.co.ltheobald.kafkatestbed.services;

import static uk.co.ltheobald.kafkatestbed.services.AuthorisationOutcome.*;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uk.co.ltheobald.kafkatestbed.FraudResult;
import uk.co.ltheobald.kafkatestbed.Transaction;
import uk.co.ltheobald.kafkatestbed.entities.TransactionEntity;
import uk.co.ltheobald.kafkatestbed.repositories.TransactionRepository;

/**
 * AuthorisationsService is responsible for managing pending transactions and processing fraud results.
 * It stores pending transactions in a map and processes fraud results by removing the corresponding transaction.
 */
@Service
public class AuthorisationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorisationService.class);

    private final TransactionRepository transactionRepository;

    public AuthorisationService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Add an incoming transaction to the ledger in a pending state. It should be processed after fraud checking.
     * @param transaction An incoming transaction to be added to the ledger.
     */
    public void addPendingTransaction(Transaction transaction) {
        transactionRepository.save(new TransactionEntity(transaction));
    }

    public AuthorisationOutcome processFraudResult(FraudResult fraudResult) {
        try {
            TransactionEntity tx = transactionRepository.findById(fraudResult.getTransactionId()).orElseThrow();
            return fraudResult.getFraudDetected() ? DECLINED : APPROVED;
        } catch (EntityNotFoundException e) {
            LOGGER.error("Could not find transaction with ID: {}", fraudResult.getTransactionId());
            return NOT_FOUND;
        }
    }
}