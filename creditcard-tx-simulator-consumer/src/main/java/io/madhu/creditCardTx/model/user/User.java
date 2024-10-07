/**
 * Author: Madhu
 * User:madhu
 * Date:23/6/24
 * Time:8:44 AM
 * Project: random-user-stream-event
 */

package io.madhu.creditCardTx.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@ToString
public class User {
    public ArrayList<Result> results;
    public Info info;

    @Override
    public String toString() {
        return "User{" +
                "results=" + results.get(0).getName().getTitle()+
                ", info=" + info +
                '}';
    }
}
