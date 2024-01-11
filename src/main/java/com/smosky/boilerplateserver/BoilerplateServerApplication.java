package com.smosky.boilerplateserver;

import com.smosky.boilerplateserver.spring.Dependency;
import com.smosky.boilerplateserver.spring.PropertyRepository;
import com.smosky.boilerplateserver.spring.SpringDependencyRepository;
import com.smosky.boilerplateserver.spring.Property;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class BoilerplateServerApplication implements CommandLineRunner {
	private final SpringDependencyRepository springDependencyRepository;
	private final PropertyRepository propertyRepository;
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

		Dependency springDataJpa = Dependency.builder()
				.id("spring-data-jpa")
				.name("Spring Data Jpa")
				.description("Spring Data Jpa Dependency")
				.build();

		springDependencyRepository.save(springDataJpa);

		Property jpaDataUrl = propertyRepository.save(Property.builder()
				.name("spring.datasource.url")
				.dependency(springDataJpa)
				.build());
		Property jpaDataUsername = propertyRepository.save(
				Property.builder()
						.name("spring.datasource.username")
						.dependency(springDataJpa)
						.build()
		);
		Property jpaDataPassword = propertyRepository.save(Property.builder()
				.name("spring.datasource.password")
				.dependency(springDataJpa)
				.build());


	}
}
