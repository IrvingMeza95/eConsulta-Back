package com.iamf.filesManagerService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@EntityScan({"com.iamf.filesCommons"})
public class FilesManagerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilesManagerServiceApplication.class, args);
	}

}
