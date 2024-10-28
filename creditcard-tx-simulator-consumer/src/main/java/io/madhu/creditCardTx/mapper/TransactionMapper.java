/**
 * Author: Madhu
 * User:madhu
 * Date:3/10/24
 * Time:1:54 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.mapper;

import io.madhu.creditCardTx.domain.user.UserCreditCardUsageInfo;
import io.madhu.creditCardTx.domain.user.UserFinancialSummary;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public UserCreditCardUsageInfo from(UserFinancialSummary usageSummary) {
        UserCreditCardUsageInfo usageInfo = new UserCreditCardUsageInfo();
        usageInfo.setTransactionCount(usageInfo.getTransactionCount());
        usageInfo.setUserEmail(usageInfo.getUserEmail());
        usageInfo.setTotalSpending(usageInfo.getTotalSpending());
        usageInfo.setUserId(usageInfo.getUserId());
        return usageInfo;
    }
}
