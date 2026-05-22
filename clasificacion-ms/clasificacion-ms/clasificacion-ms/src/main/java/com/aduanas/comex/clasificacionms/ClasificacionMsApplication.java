package com.aduanas.comex.clasificacionms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ClasificacionMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClasificacionMsApplication.class, args);
    }

}