/**
 * Author: Madhu
 * User:madhu
 * Date:12/10/24
 * Time:2:14 PM
 * Project: creditcard-tx-simulator-producer
 */

package io.madhu.creditCardTx.service.kafka;

import io.madhu.creditCardTx.model.tx.CreditCardTransaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaReadyService {

    @Value("${spring.kafka.topic}")
    private String topicName;

    @Autowired
    private KafkaProperties kafkaProperties;

    private KafkaProducer<String, CreditCardTransaction> kafkaProducer;

    public boolean isKafkaReady() {
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProperties.buildProducerProperties(null))) {
            producer.partitionsFor(topicName);
            return true;
        } catch (Exception e) {
            log.info("Failed to connect to Kafka{} ", e.getMessage());
            return false;
        }
    }
}
