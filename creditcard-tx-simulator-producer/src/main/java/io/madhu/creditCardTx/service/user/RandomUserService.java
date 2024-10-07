/**
 * Author: Madhu
 * User:madhu
 * Date:5/10/24
 * Time:12:18 PM
 * Project: creditcard-tx-simulator-producer
 */

package io.madhu.creditCardTx.service.user;

import io.madhu.creditCardTx.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class RandomUserService {

    @Autowired
    private RestClient restClient;

    public User getRandomUser() {
        User user = restClient.get()
                .retrieve()
                .body(User.class);
        return user;
    }
}
