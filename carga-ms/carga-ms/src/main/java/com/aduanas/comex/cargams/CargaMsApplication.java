package com.aduanas.comex.cargams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CargaMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CargaMsApplication.class, args);
	}

}
