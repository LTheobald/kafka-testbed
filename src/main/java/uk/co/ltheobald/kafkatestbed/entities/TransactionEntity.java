package uk.co.ltheobald.kafkatestbed.entities;

import static java.sql.Timestamp.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;
import uk.co.ltheobald.kafkatestbed.Transaction;

@Entity
@Table(name = "transactions")
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

  public TransactionEntity() {}

  public TransactionEntity(
      UUID transactionId,
      Timestamp timestamp,
      String merchantId,
      Double amount,
      String currency,
      String statementNarrative) {
    this.transactionId = transactionId;
    this.timestamp = timestamp;
    this.merchantId = merchantId;
    this.amount = amount;
    this.currency = currency;
    this.statementNarrative = statementNarrative;
  }

  /**
   * Utility constructor to convert a Transaction object to a TransactionEntity object.
   *
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

  public UUID getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(UUID transactionId) {
    this.transactionId = transactionId;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  public String getMerchantId() {
    return merchantId;
  }

  public void setMerchantId(String merchantId) {
    this.merchantId = merchantId;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getStatementNarrative() {
    return statementNarrative;
  }

  public void setStatementNarrative(String statementNarrative) {
    this.statementNarrative = statementNarrative;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    TransactionEntity that = (TransactionEntity) o;
    return Objects.equals(transactionId, that.transactionId)
        && Objects.equals(timestamp, that.timestamp)
        && Objects.equals(merchantId, that.merchantId)
        && Objects.equals(amount, that.amount)
        && Objects.equals(currency, that.currency)
        && Objects.equals(statementNarrative, that.statementNarrative);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactionId, timestamp, merchantId, amount, currency, statementNarrative);
  }
}
