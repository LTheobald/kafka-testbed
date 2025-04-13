package uk.co.ltheobald.kafkatestbed.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@SpringBootTest
class AuthSwitchTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthSwitchTest.class);

    @Autowired
    private AuthSwitch authSwitch;

    @Test
    void createAuthorisation() {
        assert true;
    }
}