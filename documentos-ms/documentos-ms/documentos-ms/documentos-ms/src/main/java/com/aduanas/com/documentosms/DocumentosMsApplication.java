package com.aduanas.com.documentosms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DocumentosMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentosMsApplication.class, args);
	}

}
