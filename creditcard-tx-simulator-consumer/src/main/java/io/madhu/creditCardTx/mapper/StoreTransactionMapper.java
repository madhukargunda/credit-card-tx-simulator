/**
 * Author: Madhu
 * User:madhu
 * Date:26/10/24
 * Time:10:42 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.mapper;

import io.madhu.creditCardTx.domain.merchant.MerchantTransactionDetails;
import io.madhu.creditCardTx.domain.store.StoreMerchantTransactionData;
import io.madhu.creditCardTx.dto.store.Stall;
import io.madhu.creditCardTx.dto.store.Store;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StoreTransactionMapper {

    public List<Store> from(List<StoreMerchantTransactionData> storeMerchantTransactionDataList) {
        return storeMerchantTransactionDataList
                .stream()
                .map(storeMerchantTransactionData ->
                        new Store(storeMerchantTransactionData.getStoreTypes().getName(),
                                storeMerchantTransactionData.getTotalStoreTransactionCount(),
                                storeMerchantTransactionData.getTotalTransactionAmount().doubleValue(),
                                this.from(storeMerchantTransactionData.getMerchantTransactionDataMap())))
                .collect(Collectors.toList());
    }

    public List<Stall> from(Map<String, MerchantTransactionDetails> merchantTransactionDetailsMap) {
        return merchantTransactionDetailsMap.entrySet()
                .stream()
                .map(entry -> new Stall(entry.getKey(),
                        entry.getValue().getTotalTransactionCount(),
                        entry.getValue().getTotalTransactionAmount().doubleValue()))
                .collect(Collectors.toList());
    }

    public Store from(StoreMerchantTransactionData storeMerchantTransactionData) {
        return new Store(storeMerchantTransactionData.getStoreTypes().getName(),
                storeMerchantTransactionData.getTotalStoreTransactionCount(),
                storeMerchantTransactionData.getTotalTransactionAmount().doubleValue(),
                this.from(storeMerchantTransactionData.getMerchantTransactionDataMap()));
    }

    public Stall from(MerchantTransactionDetails merchantTransactionDetails) {
        return new Stall(merchantTransactionDetails.getMerchantName(),
                merchantTransactionDetails.getTotalTransactionCount(),
                merchantTransactionDetails.getTotalTransactionAmount().doubleValue());
    }
}
