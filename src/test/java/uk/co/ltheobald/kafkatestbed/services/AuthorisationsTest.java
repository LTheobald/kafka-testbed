package uk.co.ltheobald.kafkatestbed.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"test-topic"}, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@DirtiesContext
class AuthorisationsTest {

    private static final String TOPIC = "test-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final BlockingQueue<ConsumerRecord<String, String>> records = new LinkedBlockingQueue<>();

    @KafkaListener(topics = TOPIC, groupId = "test-group")
    public void listen(ConsumerRecord<String, String> record) {
        records.add(record);
    }

    @Test
    void testKafkaMessageSendAndReceive() throws InterruptedException {
        // Send a message to the Kafka topic
        String message = "Test Message";
        kafkaTemplate.send(TOPIC, message);

        // Consume the message and verify
        ConsumerRecord<String, String> received = records.poll(10, java.util.concurrent.TimeUnit.SECONDS);
        assertEquals(message, received.value());
    }
}