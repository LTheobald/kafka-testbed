package uk.co.ltheobald.kafkatestbed.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import uk.co.ltheobald.kafkatestbed.FraudResult;
import uk.co.ltheobald.kafkatestbed.Transaction;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

@Component
public class Authorisations {
  private static final Logger LOGGER = LoggerFactory.getLogger(Authorisations.class);

  private KafkaTemplate<String, Object> kafkaTemplate;
  private Map<UUID, Transaction> pending = new HashMap<UUID, Transaction>();

  @Autowired
  public Authorisations(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @KafkaListener(topics = "authorisations", groupId = "authorisations")
  public void incomingAuths(ConsumerRecord<String, Transaction> transactionRecord) {
    this.pending.put(transactionRecord.value().getTransactionId(), transactionRecord.value());
  }

  @KafkaListener(topics = "fraud", groupId = "authorisations")
  public void listen(ConsumerRecord<String, FraudResult> fraudResultRecord) {
    Transaction tx = pending.remove(fraudResultRecord.value().getTransactionId());

    if (tx == null) {
      LOGGER.warn("Transaction {} not found in pending list", fraudResultRecord.value().getTransactionId());
      return;
    }

    if (fraudResultRecord.value().getFraudDetected()) {
      LOGGER.info("Transaction declined!");
      return;
    }

    LOGGER.info("Transaction approved!");
  }
}
