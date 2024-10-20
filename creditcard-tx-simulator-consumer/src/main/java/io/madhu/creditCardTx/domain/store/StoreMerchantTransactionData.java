/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:10:45 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.domain.store;

import io.madhu.creditCardTx.constants.MerchantType;
import io.madhu.creditCardTx.constants.StoreTypes;
import io.madhu.creditCardTx.domain.merchant.MerchantTransactionDetails;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Data
public class StoreMerchantTransactionData {
    private StoreTypes storeTypes;
    private Integer totalStoreTransactionCount = 0;
    private BigDecimal totalTransactionAmount;

    // Each store will have different stalls and each stall and each stall represents as the merchantType.
    private Map<String, MerchantTransactionDetails> merchantTransactionDataMap = new HashMap<>();

    public StoreMerchantTransactionData() {
        this.totalTransactionAmount = new BigDecimal(0.0);
        this.totalTransactionAmount.setScale(2, RoundingMode.HALF_UP);
    }

    public void updateMerchantTransaction(MerchantType merchantType,
                                          BigDecimal totalAmount) {
        MerchantTransactionDetails merchantTransactionDetails = this.merchantTransactionDataMap
                .getOrDefault(merchantType.name(), new MerchantTransactionDetails());
        merchantTransactionDetails.add(totalAmount);

        this.merchantTransactionDataMap.put(merchantType.name(), merchantTransactionDetails);
    }

    public void addStoreTransactionAmount(BigDecimal transactionAmount) {
        this.totalTransactionAmount = this.totalTransactionAmount.add(transactionAmount);
        this.totalStoreTransactionCount = this.totalStoreTransactionCount + 1;
    }
}
