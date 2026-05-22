package com.aduanas.com.pagosms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PagosMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PagosMsApplication.class, args);
	}

}
