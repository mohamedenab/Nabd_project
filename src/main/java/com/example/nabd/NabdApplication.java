package com.example.nabd;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Spring Boot Nabd system Rest APIs",
				version = "v1.0"
		)
)
public class NabdApplication {

	public static void main(String[] args) {
		SpringApplication.run(NabdApplication.class, args);
	}

}
