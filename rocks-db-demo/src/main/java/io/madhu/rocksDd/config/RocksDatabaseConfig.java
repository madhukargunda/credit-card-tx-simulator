/**
 * Author: Madhu
 * User:madhu
 * Date:10/10/24
 * Time:12:06 AM
 * Project: rocks-db-demo
 */

package io.madhu.rocksDd.config;


import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

@Configuration
public class RocksDatabaseConfig {

    static {
        RocksDB.loadLibrary();
    }

    @Bean
    public RocksDB rocksDB() throws RocksDBException {
        Options options = new Options();
        options.setCreateIfMissing(true);
        //Creates the RocksDB Store in the specified path
        return RocksDB.open(options, Paths.get("/tmp/data").toString());
    }
}
