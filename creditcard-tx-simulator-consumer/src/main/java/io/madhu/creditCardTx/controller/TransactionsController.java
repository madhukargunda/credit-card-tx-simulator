/**
 * Author: Madhu
 * User:madhu
 * Date:2/10/24
 * Time:11:19 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.controller;

import io.madhu.creditCardTx.service.TransactionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/transactions")
@Slf4j
public class TransactionsController {

    @Autowired
    TransactionsService transactionsService;

    @GetMapping("/countByMerchant")
    public ResponseEntity<?> countByMerchant() {
        log.info("Received request for total count by merchant:");
        return ResponseEntity.ok(transactionsService.getByMerchantCount());
    }

    @GetMapping("/totalSpendingByUser")
    public ResponseEntity<?> totalSpendByUser() {
        log.info("Received request for total spending by user:");
        return ResponseEntity.ok(transactionsService.getTotalSpendingByUser());
    }
}