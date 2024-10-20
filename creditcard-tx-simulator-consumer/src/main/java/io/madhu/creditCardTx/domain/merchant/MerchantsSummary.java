/**
 * Author: Madhu
 * User:madhu
 * Date:20/10/24
 * Time:12:06 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.domain.merchant;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class MerchantsSummary {

    private String merchantName;

    private BigDecimal allMerchantTotalAmount;

    private Long allMerchantTransactionsCount;

    public MerchantsSummary() {
        this.allMerchantTotalAmount = new BigDecimal(0.0);
        this.allMerchantTotalAmount.setScale(2, RoundingMode.HALF_UP);
        this.allMerchantTransactionsCount = 0l;
    }

    public void addTransactionAmount(BigDecimal totalAmount, Long merchantTxCount) {
        this.allMerchantTotalAmount = this.allMerchantTotalAmount.add(totalAmount);
        this.allMerchantTransactionsCount = this.allMerchantTransactionsCount + merchantTxCount;
    }
}
