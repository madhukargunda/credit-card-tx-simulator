/**
 * Author: Madhu
 * User:madhu
 * Date:10/10/24
 * Time:12:10 AM
 * Project: rocks-db-demo
 */

package io.madhu.rocksDd.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RocksDBRepository {

    @Autowired
    private RocksDB rocksDB;

    @Autowired
    ObjectMapper objectMapper;

   public <V> void put(String key,V value) throws RocksDBException, JsonProcessingException {
       String asString = objectMapper.writeValueAsString(value);
       rocksDB.put(key.getBytes(),asString.getBytes());
   }
}
