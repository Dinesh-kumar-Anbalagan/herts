package com.herts.shipment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages =  {"com.herts.orchestrator",
"com.herts.shipment"})
public class ShipmentApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShipmentApplication.class, args);
	}
}
