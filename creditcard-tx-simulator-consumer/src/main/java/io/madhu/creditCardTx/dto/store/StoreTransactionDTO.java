/**
 * Author: Madhu
 * User:madhu
 * Date:26/10/24
 * Time:10:44 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.dto.store;

import lombok.Data;

import java.util.List;

@Data
public class StoreTransactionDTO {
    private List<Store> storeList;
}
