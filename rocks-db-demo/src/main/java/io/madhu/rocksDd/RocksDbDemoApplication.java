package io.madhu.rocksDd;

import io.madhu.rocksDd.service.RocksDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RocksDbDemoApplication implements CommandLineRunner {

	@Autowired
	private RocksDBService rocksDBService;

	public static void main(String[] args) {
		SpringApplication.run(RocksDbDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		rocksDBService.put("user:",new String("madhukar"));
	}
}
