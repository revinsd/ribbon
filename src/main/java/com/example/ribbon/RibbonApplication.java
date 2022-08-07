package com.example.ribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootApplication
public class RibbonApplication {


	public static void main(String[] args) {
		SpringApplication.run(RibbonApplication.class, args);

	}

}
