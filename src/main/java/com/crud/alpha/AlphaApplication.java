package com.crud.alpha;

import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableJpaAuditing
@RestController
public class AlphaApplication {


	@PostConstruct
	public void initMercadoPago() {
		MercadoPagoConfig.setAccessToken("APP_USR-4660902402181456-051421-a0dc72f950d732f8c86b2ac9f49a8c2c-2442411888");
	}

	public static void main(String[] args) {
		SpringApplication.run(AlphaApplication.class, args);
	}




}
