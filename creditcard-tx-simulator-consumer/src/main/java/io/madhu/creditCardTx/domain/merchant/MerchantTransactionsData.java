/**
 * Author: Madhu
 * User:madhu
 * Date:3/10/24
 * Time:12:08 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.domain.merchant;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
public class MerchantTransactionsData<K, V> {

    private Map<String, MerchantsSummary> allMerchantsSummary = new HashMap<>();

    public void add(String name, BigDecimal amount) {
        MerchantsSummary merchantsSummary = allMerchantsSummary
                .getOrDefault(name, new MerchantsSummary());
        merchantsSummary.addTransactionAmount(merchantsSummary.getAllMerchantTotalAmount(),
                merchantsSummary.getAllMerchantTransactionsCount());
    }
}

