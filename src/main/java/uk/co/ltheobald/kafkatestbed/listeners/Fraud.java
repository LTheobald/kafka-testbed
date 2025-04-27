package uk.co.ltheobald.kafkatestbed.listeners;

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
import uk.co.ltheobald.kafkatestbed.services.FraudService;

@Component
public class Fraud {
  private static final Logger LOGGER = LoggerFactory.getLogger(Fraud.class);

  public static final String TOPIC = "fraud";
  private KafkaTemplate<String, Object> kafkaTemplate;
  private FraudService fraudService;

  @Autowired
  public Fraud(KafkaTemplate<String, Object> kafkaTemplate, FraudService fraudService) {
    this.kafkaTemplate = kafkaTemplate;
    this.fraudService = fraudService;
  }

  @KafkaListener(topics = "authorisations", groupId = "fraud-auth-listeners")
  public void listen(ConsumerRecord<String, Transaction> transactionRecord) {
    FraudResult fraudResult = fraudService.checkTransactionForFraud(transactionRecord.value());
    kafkaTemplate.send(TOPIC, fraudResult.getFraudResultId().toString(), fraudResult);
  }
}