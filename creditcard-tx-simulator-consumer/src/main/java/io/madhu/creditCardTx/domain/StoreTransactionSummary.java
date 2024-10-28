/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:10:45 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.domain;

import io.madhu.creditCardTx.constants.MerchantType;
import io.madhu.creditCardTx.constants.StoreTypes;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class StoreTransactionSummary {
    private StoreTypes storeTypes;
    private Map<String, MerchantTransactionSummary>
            merchantTransactionSummaryMap = new HashMap<>();

    public void addMerchantTransactionSummary(String merchantType,
                                              MerchantTransactionSummary merchantTransactionSummary) {
        MerchantTransactionSummary merTxSummary = this.merchantTransactionSummaryMap
                .getOrDefault(merchantType, new MerchantTransactionSummary());
        merTxSummary.addMerchantTransactionSummary(
                merchantTransactionSummary.getTotalTransactionAmount());
    }

    public void addMerchantTransactionSummary(MerchantType merchantType,
                                               BigDecimal totalAmount) {
        MerchantTransactionSummary merTxSummary = this.merchantTransactionSummaryMap
                .getOrDefault(merchantType.name(), new MerchantTransactionSummary());
        merTxSummary.addMerchantTransactionSummary(totalAmount);
    }
}
