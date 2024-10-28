/**
 * Author: Madhu
 * User:madhu
 * Date:2/10/24
 * Time:11:23 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.dto.merchant;

import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


@Data
@Getter

public class MerchantTransactionsResponse {

    private Map merchantTransactionCount;

    public MerchantTransactionsResponse() {
        merchantTransactionCount = new HashMap<>();
    }
}
