/**
 * Author: Madhu
 * User:madhu
 * Date:3/10/24
 * Time:10:40 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.repository.base;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class KeyValueStateStoreRepository {

    @Autowired
    StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    /**
     * reusable method for getting the store names
     *
     * @param storeName
     * @param keyType
     * @param valueType
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> ReadOnlyKeyValueStore<K, V> getStore(String storeName, Class<K> keyType, Class<V> valueType) {
        KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        if (Objects.nonNull(kafkaStreams)
                && kafkaStreams.state() == KafkaStreams.State.RUNNING) {
            return kafkaStreams.store(StoreQueryParameters.fromNameAndType(storeName, QueryableStoreTypes.<K, V>keyValueStore()));
        } else
            throw new IllegalStateException("Store is not ready to access, Please wait for some time");
    }

    /**
     * Reusable method for getting the value based on the key
     *
     * @param storeName
     * @param key
     * @param keyType
     * @param valueType
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> V getValue(String storeName, K key, Class<K> keyType, Class<V> valueType) {
        ReadOnlyKeyValueStore<K, V> store = getStore(storeName, keyType, valueType);
        return store.get(key);
    }

}
