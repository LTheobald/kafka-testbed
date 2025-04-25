package uk.co.ltheobald.kafkatestbed.services;

import java.time.Instant;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import uk.co.ltheobald.kafkatestbed.FraudResult;
import uk.co.ltheobald.kafkatestbed.Transaction;

@Component
public class Fraud {
  private static final Logger LOGGER = LoggerFactory.getLogger(Fraud.class);

  public static final String TOPIC = "fraud";
  private KafkaTemplate<String, Object> kafkaTemplate;

  @Autowired
  public Fraud(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @KafkaListener(topics = "authorisations", groupId = "fraud-auth-listeners")
  public void listen(ConsumerRecord<String, Transaction> transactionRecord) {
    FraudResult fraudResult =
        FraudResult.newBuilder()
            .setTimestamp(Instant.now())
            .setFraudDetected(false)
            .setTransactionId(transactionRecord.value().getTransactionId())
            .setFraudResultId(UUID.randomUUID())
            .build();

    // If an amount ends in .99, we'll use that to act as if something is fraud
    if (transactionRecord.value().getAmount() % 1 == 0.99) {
      LOGGER.info(
          "Fraud detected for transaction ID: {} with amount: {}",
          transactionRecord.key(), transactionRecord.value().getAmount());
      fraudResult.setFraudDetected(true);
    }

    kafkaTemplate.send(TOPIC, fraudResult.getFraudResultId().toString(), fraudResult);
  }
}
