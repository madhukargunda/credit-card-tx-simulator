/**
 * Author: Madhu
 * User:madhu
 * Date:6/10/24
 * Time:7:25 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Component
public class GlobalRestControllerAdvice {

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleException(IllegalStateException ex) {
        log.error("Service unavailable: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatusCode.valueOf(500).toString());
        errorResponse.setErrorMessage(ex.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    static class ErrorResponse {
        private String errorCode;
        private String errorMessage;
    }
}
