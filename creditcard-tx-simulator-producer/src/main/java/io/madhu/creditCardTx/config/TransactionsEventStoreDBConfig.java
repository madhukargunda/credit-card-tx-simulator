/**
 * Author: Madhu
 * User:madhu
 * Date:29/9/24
 * Time:1:57 PM
 * Project: creditcard-tx-simulator-kstream
 */

package io.madhu.creditCardTx.config;


import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientSettings;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionsEventStoreDBConfig {

    @Value("${spring.eventStore.url}")
    private String eventStoreDBurl;

    @Bean
    public EventStoreDBClient eventStoreDBClient() {
        EventStoreDBClientSettings eventStoreDBClientSettings = EventStoreDBConnectionString.parseOrThrow(eventStoreDBurl);
        return EventStoreDBClient.create(eventStoreDBClientSettings);
    }
}
