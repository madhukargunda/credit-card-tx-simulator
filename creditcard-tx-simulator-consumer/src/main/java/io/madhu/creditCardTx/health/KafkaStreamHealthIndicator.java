/**
 * Author: Madhu
 * User:madhu
 * Date:20/10/24
 * Time:11:13 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.health;

import org.apache.kafka.streams.KafkaStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class KafkaStreamHealthIndicator implements HealthIndicator {

    @Autowired
    private StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    @Override
    public Health health() {
        KafkaStreams.State state = streamsBuilderFactoryBean.getKafkaStreams().state();
        if (state == KafkaStreams.State.RUNNING) {
            return Health.up().withDetail("Kafka Streams State", state.toString()).build();
        } else {
            return Health.down().withDetail("Kafka Streams State", state.toString()).build();
        }
    }
}
