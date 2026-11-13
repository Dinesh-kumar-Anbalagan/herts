package com.herts.esData;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.herts.esData",
		"com.herts.orchestrator",
		"com.herts.dbService"
})
public class EsDataApplication {
	public static void main(String[] args) {
		SpringApplication.run(EsDataApplication.class, args);
	}
}
