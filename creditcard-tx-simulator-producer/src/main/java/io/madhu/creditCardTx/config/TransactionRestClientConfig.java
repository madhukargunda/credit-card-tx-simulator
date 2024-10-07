/**
 * Author: Madhu
 * User:madhu
 * Date:5/10/24
 * Time:3:07 PM
 * Project: creditcard-tx-simulator-producer
 */

package io.madhu.creditCardTx.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class TransactionRestClientConfig {

    @Value("${spring.application.randomUserUri}")
    String uri;

    @Bean
    RestClient restClient(){
        return  RestClient.create(uri);
    }
}
