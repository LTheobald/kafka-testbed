package uk.co.ltheobald.kafkatestbed.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.co.ltheobald.kafkatestbed.Transaction;

import java.sql.Timestamp;
import java.util.UUID;

import static java.sql.Timestamp.*;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

    @Id
    @Column(name = "transaction_id", nullable = false, updatable = false)
    private UUID transactionId;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    @Column(name = "merchant_id", nullable = false, length = 255)
    private String merchantId;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "statement_narrative", nullable = false)
    private String statementNarrative;

    /**
     * Utility constructor to convert a Transaction object to a TransactionEntity object.
     * @param transaction An Transaction object (from Avro) to be converted
     */
    public TransactionEntity(Transaction transaction) {
        this.transactionId = transaction.getTransactionId();
        this.timestamp = from(transaction.getTimestamp());
        this.merchantId = transaction.getMerchantId().toString();
        this.amount = transaction.getAmount();
        this.currency = transaction.getCurrency().toString();
        this.statementNarrative = transaction.getStatementNarrative().toString();
    }
}