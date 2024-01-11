package com.smosky.boilerplateserver;

import com.smosky.boilerplateserver.spring.SpringDependency;
import com.smosky.boilerplateserver.spring.SpringDependencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication

public class BoilerplateServerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BoilerplateServerApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
//		Map<String,String> jpaProperties = new HashMap<>();
//		jpaProperties.put("spring.datasource.url",)
//		SpringDependency springDataJpa = SpringDependency.builder()
//				.id("spring-data-jpa")
//				.name("name")
//				.description("Spring Data Jpa Description")
//				.map()
//				.build();
//		repository.save()
	}
}
