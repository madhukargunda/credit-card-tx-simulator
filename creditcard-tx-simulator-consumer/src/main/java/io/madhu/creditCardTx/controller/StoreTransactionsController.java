/**
 * Author: Madhu
 * User:madhu
 * Date:2/10/24
 * Time:11:19 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.controller;

import io.madhu.creditCardTx.domain.store.StoreTransactionData;
import io.madhu.creditCardTx.dto.store.Stall;
import io.madhu.creditCardTx.dto.store.Store;
import io.madhu.creditCardTx.exception.ResourceNotFoundException;
import io.madhu.creditCardTx.service.StoreTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/transactions")
@Slf4j
@Tag(name = "Store Transactions Summary",
        description = "Store Transactions Summary")
public class StoreTransactionsController {

    @Autowired
    StoreTransactionService storeTransactionService;

    @GetMapping("/stores/summary")
    @Operation(
            summary = "Fetch all store wise transactions summary",
            description = "Fetch all store wise transactions summary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    public Flux<List<Store>> getAllStoreSummaries() {
        return Flux.interval(Duration.ofSeconds(20))
                .flatMap(tick -> Mono.fromCallable(() -> storeTransactionService.getAllStoreSummaries()));
    }

    @GetMapping("/stores/{storeName}/summary")
    @Operation(
            summary = "Fetch store wise transactions summary specific to storeName",
            description = "fetches all plant entities and their data from data source")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StoreTransactionData.class))})
    })
    public Store getStoreSummaryByStore(@Parameter(name = "storeName")
                                              @Valid @NotBlank @NotEmpty @PathVariable(name = "storeName", required = true) String storeName) {
        log.info("The Pathvariable storename {}", storeName);
        if (!Objects.nonNull(storeName)) {
            throw new ResourceNotFoundException("storeName is missing");
        }
//        return Flux.interval(Duration.ofSeconds(20))
//                .flatMap(tick -> Mono.fromCallable(() -> storeTransactionService.getStoreSummary(storeName)));
        return storeTransactionService.getStoreSummary(storeName);
    }

    @GetMapping("/stores/{storeName}/merchants/summary")
    @Operation(
            summary = "Fetch store wise transactions summary specific to storeName",
            description = "fetches all plant entities and their data from data source")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StoreTransactionData.class))})
    })
    public List<Stall> getMerchantSummaryByStore(@Parameter(name = "storeName")
                                                       @Valid @NotBlank @NotEmpty @PathVariable(value = "storeName", required = true) String storeName) {
        if (!Objects.nonNull(storeName)) {
            throw new ResourceNotFoundException("storeName is missing");
        }
//        return Flux.interval(Duration.ofSeconds(20))
//                .flatMap(tick -> Mono.fromCallable(() -> storeTransactionService.getAllMerchantSummariesByStore(storeName)));
         return  storeTransactionService.getAllMerchantSummariesByStore(storeName);
    }

    @GetMapping("/stores/{storeName}/merchants/{merchantName}/summary")
    @Operation(
            summary = "Fetch store wise transactions summary specific to storeName",
            description = "fetches all plant entities and their data from data source")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StoreTransactionData.class))})
    })
    public Stall getMerchantSummaryByStore(@Parameter(name = "storeName")
                                                 @Valid @NotBlank @NotEmpty @PathVariable(name = "storeName", required = true) String storeName, @Parameter(name = "merchantName")
                                                 @NotBlank @NotEmpty @PathVariable(name = "merchantName", required = true) String merchantName) {
        if (!Objects.nonNull(storeName)) {
            throw new ResourceNotFoundException("storeName is missing");
        }
        if (!Objects.nonNull(merchantName)) {
            throw new ResourceNotFoundException("merchantName is missing");
        }
       /** return Flux.interval(Duration.ofSeconds(20))
                .flatMap(tick -> Mono.fromCallable(() -> storeTransactionService.getMerchantSummariesByStore(storeName, merchantName))); */
        return storeTransactionService.getMerchantSummariesByStore(storeName, merchantName);
    }
}