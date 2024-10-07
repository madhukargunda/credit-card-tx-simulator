/**
 * Author: Madhu
 * User:madhu
 * Date:29/9/24
 * Time:6:17 PM
 * Project: creditcard-tx-simulator-kstream
 */

package io.madhu.creditCardTx.service.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class EventSerdeService {

    private ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public String serialize(Object event) {
        return objectMapper.writeValueAsString(event);
    }

    @SneakyThrows
    public <T> T deserialize(String json, Class<T> eventType) {
        return objectMapper.readValue(json, eventType);
    }
}
