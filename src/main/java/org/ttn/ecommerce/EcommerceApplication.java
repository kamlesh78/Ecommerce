package org.ttn.ecommerce;


import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.logging.Logger;

@SpringBootApplication
@EnableAsync
public class EcommerceApplication {


	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

}
