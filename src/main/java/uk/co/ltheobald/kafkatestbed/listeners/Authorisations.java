package uk.co.ltheobald.kafkatestbed.listeners;

import static uk.co.ltheobald.kafkatestbed.services.AuthorisationOutcome.*;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import uk.co.ltheobald.kafkatestbed.FraudResult;
import uk.co.ltheobald.kafkatestbed.Transaction;
import uk.co.ltheobald.kafkatestbed.services.AuthorisationOutcome;
import uk.co.ltheobald.kafkatestbed.services.AuthorisationService;

/**
 * A class to listen for incoming authorisation requests on a Kafka topic and deal with them apprioriately.
 */
@Component
public class Authorisations {
  private static final Logger LOGGER = LoggerFactory.getLogger(Authorisations.class);

  private KafkaTemplate<String, Object> kafkaTemplate;
  private AuthorisationService authorisationService;

  @Autowired
  public Authorisations(KafkaTemplate<String, Object> kafkaTemplate, AuthorisationService authorisationService) {
    this.kafkaTemplate = kafkaTemplate;
    this.authorisationService = authorisationService;
  }

  @KafkaListener(topics = "authorisations", groupId = "authorisations")
  public void incomingAuths(ConsumerRecord<String, Transaction> transactionRecord) {
    authorisationService.addPendingTransaction(transactionRecord.value());
  }

  @KafkaListener(topics = "fraud", groupId = "authorisations")
  public void listen(ConsumerRecord<String, FraudResult> fraudResultRecord) {
    AuthorisationOutcome result = authorisationService.processFraudResult(fraudResultRecord.value());

    switch (result) {
      case NOT_FOUND:
        LOGGER.info("Transaction not found!");
        return;
      case APPROVED:
        LOGGER.info("Transaction approved!");
        return;
      case DECLINED:
        LOGGER.info("Transaction declined :(");
        return;
    }
  }
}
