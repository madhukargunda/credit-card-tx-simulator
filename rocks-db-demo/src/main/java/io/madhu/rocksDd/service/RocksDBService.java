/**
 * Author: Madhu
 * User:madhu
 * Date:10/10/24
 * Time:12:09 AM
 * Project: rocks-db-demo
 */

package io.madhu.rocksDd.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.madhu.rocksDd.repository.RocksDBRepository;
import org.rocksdb.RocksDBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RocksDBService {

    @Autowired
    RocksDBRepository rocksDBRepository;

    @Autowired
    ObjectMapper objectMapper;

    public <T> void put(String key, T value) throws JsonProcessingException, RocksDBException {
        byte[] bytes = objectMapper.writeValueAsBytes(value);
        rocksDBRepository.put(key,bytes);
    }
}
