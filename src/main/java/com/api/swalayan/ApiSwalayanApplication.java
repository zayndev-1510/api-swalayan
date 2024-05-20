package com.api.swalayan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ApiSwalayanApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiSwalayanApplication.class, args);
	}

}
