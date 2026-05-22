package com.aduanas.comex.riesgo_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients // Habilita el cliente Feign para conectar con carga-ms
@SpringBootApplication // Limpio, sin exclusiones para que use la Base de Datos e inicie Security
public class RiesgoMsApplication {
    public static void main(String[] args) {
        SpringApplication.run(RiesgoMsApplication.class, args);
    }
}