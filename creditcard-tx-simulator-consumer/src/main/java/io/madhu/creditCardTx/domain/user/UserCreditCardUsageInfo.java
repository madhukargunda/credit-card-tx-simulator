/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:4:28 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.domain.user;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Deprecated
public class UserCreditCardUsageInfo {

    public UserCreditCardUsageInfo() {
        this.totalSpending = new BigDecimal("0.0");
        this.totalSpending.setScale(2, RoundingMode.HALF_UP);
        this.transactionCount = 0l;
    }

    private String userId;

    private String userEmail;

    private BigDecimal totalSpending;

    private Long transactionCount;

}
