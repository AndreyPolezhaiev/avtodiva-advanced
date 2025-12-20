package com.polezhaiev.avtodiva;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import javax.swing.*;

@EnableCaching
@SpringBootApplication
public class AvtoDivaApplication {
	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");

        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(AvtoDivaApplication.class, args);
	}
}
