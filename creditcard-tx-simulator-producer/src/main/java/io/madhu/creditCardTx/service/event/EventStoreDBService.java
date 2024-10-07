/**
 * Author: Madhu
 * User:madhu
 * Date:29/9/24
 * Time:1:58 PM
 * Project: creditcard-tx-simulator-kstream
 */

package io.madhu.creditCardTx.service.event;


import com.eventstore.dbclient.EventData;
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.ReadStreamOptions;
import com.eventstore.dbclient.ResolvedEvent;
import io.madhu.creditCardTx.event.CreditCardTransactionBaseEvent;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventStoreDBService {

    @Value("${spring.eventStore.streamName}")
    private String STREAM_NAME ;

    @Autowired
    EventStoreDBClient eventStoreDBClient;

    @Autowired
    EventSerdeService eventSerializerService;

    @SneakyThrows
    public void appendEvent(String eventName, CreditCardTransactionBaseEvent creditCardTransactionBaseEvent) {
        String streamName = String.format(STREAM_NAME, creditCardTransactionBaseEvent.getTransactionId());
        String serialize = eventSerializerService.serialize(creditCardTransactionBaseEvent);
        EventData eventData = EventData.builderAsJson(eventName, serialize).build();
        eventStoreDBClient.appendToStream(streamName, eventData).join();
    }

    @SneakyThrows
    public List<ResolvedEvent> readEvents(String streamName) {
        ReadStreamOptions options = ReadStreamOptions.get().forwards().fromStart();
        return eventStoreDBClient.readStream(streamName, options).get().getEvents();
    }
}
