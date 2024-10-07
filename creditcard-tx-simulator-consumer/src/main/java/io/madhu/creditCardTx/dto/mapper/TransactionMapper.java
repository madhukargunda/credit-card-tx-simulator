/**
 * Author: Madhu
 * User:madhu
 * Date:3/10/24
 * Time:1:54 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.dto.mapper;

import io.madhu.creditCardTx.domain.MerchantTransactionsData;
import io.madhu.creditCardTx.dto.MerchantTransactionsResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TransactionMapper {

    public <K,V> MerchantTransactionsResponse  from(MerchantTransactionsData merchantTransactionsData,Class<K> keyType, Class<V> valueType ) {
        HashMap<K,V> responseMap = new HashMap<K,V>();
        responseMap.putAll(merchantTransactionsData.getData());
        MerchantTransactionsResponse response = new MerchantTransactionsResponse();
        response.setMerchantTransactionCount(responseMap);
        return response;
    }
}
