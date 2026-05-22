package com.aduanas.comex.notificacion_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class NotificacionMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificacionMsApplication.class, args);
	}

}
