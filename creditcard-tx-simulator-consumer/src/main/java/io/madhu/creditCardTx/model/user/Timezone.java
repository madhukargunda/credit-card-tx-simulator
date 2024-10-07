/**
 * Author: Madhu
 * User:madhu
 * Date:23/6/24
 * Time:8:43 AM
 * Project: random-user-stream-event
 */

package io.madhu.creditCardTx.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Timezone {

    public String offset;
    public String description;
}
