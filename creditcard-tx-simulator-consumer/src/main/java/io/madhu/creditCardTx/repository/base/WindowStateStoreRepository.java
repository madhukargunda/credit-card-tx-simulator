/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:9:24 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.repository.base;


import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.apache.kafka.streams.state.WindowStoreIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Objects;

@Repository
public class WindowStateStoreRepository {

    @Autowired
    StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    /**
     * WindowStore to retrieve the data
     *
     * @param storeName
     * @param keyType
     * @param valueType
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> ReadOnlyWindowStore<K, V> getWindowStore(String storeName, Class<K> keyType, Class<V> valueType) {
        KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        if (Objects.nonNull(kafkaStreams)
                && kafkaStreams.state() == KafkaStreams.State.RUNNING) {
            return kafkaStreams.store(StoreQueryParameters.fromNameAndType(storeName, QueryableStoreTypes.<K, V>windowStore()));
        } else
            throw new IllegalStateException("Store is not ready to access, Please wait for some time");
    }

    public <K, V> WindowStoreIterator<V> getValue(String storeName, K key, Class<K> keyType, Class<V> valueType, long timeFrom, long timeTo) {
        ReadOnlyWindowStore<K, V> store = getWindowStore(storeName, keyType, valueType);
        return store.<K>fetch(key, Instant.ofEpochMilli(timeFrom), Instant.ofEpochMilli(timeTo));
    }
}
