package uk.co.ltheobald.kafkatestbed.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.co.ltheobald.kafkatestbed.Transaction;
import uk.co.ltheobald.kafkatestbed.services.AuthSwitchService;

@Component
class AuthSwitch {
  public static final String TOPIC = "authorisations";

  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final AuthSwitchService authSwitchService;

  @Autowired
  public AuthSwitch(KafkaTemplate<String, Object> kafkaTemplate, AuthSwitchService authSwitchService) {
    this.kafkaTemplate = kafkaTemplate;
    this.authSwitchService = authSwitchService;
  }

  /** Create a random transaction every X seconds */
  @Scheduled(fixedRate = 5000)
  public void createAuthorisations() {
    Transaction tx = authSwitchService.createIncomingTransaction();
    kafkaTemplate.send(TOPIC, tx.getTransactionId().toString(), tx);
  }
}
