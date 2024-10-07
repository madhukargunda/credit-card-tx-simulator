/**
 * Author: Madhu
 * User:madhu
 * Date:23/6/24
 * Time:8:40 AM
 * Project: random-user-stream-event
 */

package io.madhu.creditCardTx.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Location {

    public Street street;
    public String city;
    public String state;
    public String country;
    public String postcode;
    public Coordinates coordinates;

}
