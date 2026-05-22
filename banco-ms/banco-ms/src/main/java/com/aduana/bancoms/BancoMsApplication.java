package com.aduana.bancoms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BancoMsApplication {
    public static void main(String[] args) {
        SpringApplication.run(BancoMsApplication.class, args);
    }
}