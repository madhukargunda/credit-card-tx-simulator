/**
 * Author: Madhu
 * User:madhu
 * Date:23/6/24
 * Time:8:45 AM
 * Project: random-user-stream-event
 */

package io.madhu.creditCardTx.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class Registered {
    public Date date;
    public int age;
}
