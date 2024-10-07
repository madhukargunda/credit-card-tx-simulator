/**
 * Author: Madhu
 * User:madhu
 * Date:3/10/24
 * Time:12:08 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.domain;

import lombok.Data;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class MerchantTransactionsData<K,V> {

    private Map<K,V> data =  new HashMap<>();

    public void add(K k, V v){
        this.data.put(k,v);
    }
}

