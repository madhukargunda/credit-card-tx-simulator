/**
 * Author: Madhu
 * User:madhu
 * Date:23/6/24
 * Time:8:39 AM
 * Project: random-user-stream-event
 */

package io.madhu.creditCardTx.model.user;

import lombok.Data;

@Data
public class Login {
    public String uuid;
    public String username;
    public String password;
    public String salt;
    public String md5;
    public String sha1;
    public String sha256;
}
