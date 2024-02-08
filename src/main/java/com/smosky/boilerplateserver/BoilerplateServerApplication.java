package com.smosky.boilerplateserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smosky.boilerplateserver.spring.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class BoilerplateServerApplication implements CommandLineRunner {

  private final DependencyRepository dependencyRepository;
  private final PropertyRepository propertyRepository;

  private final SelectOptionRepository selectOptionRepository;
  private final LinkRepository linkRepository;
  private final TypeRepository typeRepository;
  private final BoilerplateRepository boilerplateRepository;
  private final ReviewRepository reviewRepository;
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static void main(String[] args) {
    SpringApplication.run(BoilerplateServerApplication.class, args);
  }


  @Override
  public void run(String... args) throws Exception {
//		String springJsonString = getSpringJsonObject("spring-dependencies.json");
//		saveSpringType(springJsonString);

    createBoilerplateFromJson();
    convertOriginal();
  }

  private void createBoilerplateFromJson() throws JsonProcessingException {
    String jsonString = getSpringJsonObject("boilerplate.json");
    JsonNode nodes = objectMapper.readTree(jsonString);

    for (JsonNode node : nodes) {
      List<String> dependencies = new ArrayList<>();
      List<String> features = new ArrayList<>();

      if (node.path("dependencies").isArray()) {
        for (JsonNode dependency : node.path("dependencies")) {
          dependencies.add(dependency.asText());
        }
      }
      if (node.path("features").isArray()) {
        for (JsonNode feature : node.path("features")) {
          features.add(feature.asText());
        }
      }

      Boilerplate boilerplate = boilerplateRepository.save(Boilerplate.builder()
          .name(node.path("name").asText())
          .description(node.path("description").asText())
          .dependencies(dependencies)
          .features(features)
          .previewLink(node.path("previewLink").asText())
          .thumbnail(node.path("thumbnail").asText())
          .build());
      Review review = Review.builder()
          .name("loctran")
          .email("tranphuocloc070699@gmail.com")
          .content("Good content")
          .star(5)
          .boilerplate(boilerplate)
          .build();
      Review review2 = Review.builder()
          .name("loctran")
          .email("tranphuocloc070699@gmail.com")
          .content("Bad Bad Bad!!!")
          .star(1)
          .boilerplate(boilerplate)
          .build();
      Review review3 = Review.builder()
          .name("loctran")
          .email("tranphuocloc070699@gmail.com")
          .content("Bad Bad Bad!!!")
          .star(1)
          .boilerplate(boilerplate)
          .build();
      Review review4 = Review.builder()
          .name("loctran")
          .email("tranphuocloc070699@gmail.com")
          .content("Bad Bad Bad!!!")
          .star(1)
          .boilerplate(boilerplate)
          .build();
    /*  reviewRepository.save(review);
      reviewRepository.save(review2);
      reviewRepository.save(review3);
      reviewRepository.save(review4);*/

    }
  }

  private void convertOriginal() throws JsonProcessingException {
    String jsonString = getSpringJsonObject("original.json");
    JsonNode nodes = objectMapper.readTree(jsonString).path("dependencies");

    for (JsonNode node : nodes) {
      DependencyType type = typeRepository.save(
          DependencyType.builder()
              .name(node.path("name").asText())
              .languageType(LanguageType.SPRING)
              .build()
      );
      JsonNode dependenciesNodes = node.path("content");
      convertDependencyOriginal(dependenciesNodes, type);
    }
  }

  private void convertDependencyOriginal(JsonNode dependenciesNodes, DependencyType type) {
    for (JsonNode dependencyNode : dependenciesNodes) {
      JsonNode compatibilityRangeNode = dependencyNode.path("compatibilityRange");
      String[] splitResult = new String[]{};
      if (!compatibilityRangeNode.asText().isEmpty()) {
        String compatibilityRange = compatibilityRangeNode.asText();
        if (compatibilityRange.startsWith("[")) {
          compatibilityRange = compatibilityRange.substring(1);
        }
        if (compatibilityRange.endsWith(")")) {
          compatibilityRange = compatibilityRange.substring(0, compatibilityRange.length() - 1);
        }

        splitResult = compatibilityRange.split(",");


      }
      Dependency dependency = dependencyRepository.save(
          Dependency.builder()
              .id(dependencyNode.path("id").asText())
              .name(dependencyNode.path("name").asText())
              .description(dependencyNode.path("description").asText())
              .compatibilityRanges(Arrays.stream(splitResult).toList())
              .type(type)
              .build()
      );
      convertLinksOriginal(dependency);
      convertPropertiesAndOptionsOriginal(dependency);
    }
  }

	/*private static List<String> convertJson(String json) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(json);

		List<String> result = new ArrayList<>();

		JsonNode compatibilityRangeNode = root.path("compatibilityRange");

		if (compatibilityRangeNode.isArray()) {
			Iterator<JsonNode> elements = compatibilityRangeNode.elements();

			while (elements.hasNext()) {
				JsonNode element = elements.next();
				result.add(element.asText());
			}
		} else if (compatibilityRangeNode.isTextual()) {
			result.add(compatibilityRangeNode.asText());
		}

		return result;
	}*/


  private void convertPropertiesAndOptionsOriginal(Dependency dependency) {
    if (dependency.getId().equals("data-jpa")) {
      propertyRepository.save(
          Property.builder()
              .id("spring.datasource.url")
              .title("Url")
              .value("jdbc:postgresql://localhost:5432/postgres")
              .toolTip("This is url to connect to your database")
              .dependency(dependency)
              .build()
      );

      propertyRepository.save(
          Property.builder()
              .id("spring.datasource.username")
              .title("Url")
              .value("root")
              .toolTip("This is username to authentication with your database")
              .dependency(dependency)
              .build()
      );
      propertyRepository.save(
          Property.builder()
              .id("spring.datasource.password")
              .title("Password")
              .value("root")
              .toolTip("This is password to authentication with your database")
              .dependency(dependency)
              .build()
      );
      Property property4 = propertyRepository.save(
          Property.builder()
              .id("spring.main.web-application-type")
              .title("Application type")
              .value("servlet")
              .toolTip("This is type of application")
              .dependency(dependency)
              .build()
      );
      Property property5 = propertyRepository.save(
          Property.builder()
              .id("spring.jpa.database-platform")
              .title("Database type")
              .value("org.hibernate.dialect.PostgreSQLDialect")
              .isDisable(true)
              .toolTip("This is database type of application")
              .dependency(dependency)
              .build()
      );
      Property property6 = propertyRepository.save(
          Property.builder()
              .id("spring.jpa.hibernate.ddl-auto")
              .title("Placeholder")
              .value("create-drop")
              .toolTip("This is placeholder")
              .dependency(dependency)
              .build()
      );
//			Property property5 = propertyRepository.save(
//					Property.builder()
//							.id("spring.main.web-application-type")
//							.title("Application type")
//							.value("servlet")
//							.toolTip("This is type of application")
//							.dependency(dependency)
//							.build()
//			);
//			Property property5 = propertyRepository.save(
//					Property.builder()
//							.id("spring.main.web-application-type")
//							.title("Application type")
//							.value("servlet")
//							.toolTip("This is type of application")
//							.dependency(dependency)
//							.build()
//			);
      selectOptionRepository.save(SelectOption.builder()
          .label("Servlet")
          .value("servlet")
          .property(property4)
          .build());
    }
  }

  private void convertLinksOriginal(Dependency dependency) {
    linkRepository.save(
        Link.builder()
            .name("maven")
            .title("Maven")
            .url("")
            .dependency(dependency)
            .build()
    );
    linkRepository.save(
        Link.builder()
            .name("github")
            .title("Github")
            .url("")
            .dependency(dependency)
            .build()
    );
    linkRepository.save(
        Link.builder()
            .name("homepage")
            .title("Homepage")
            .url("")
            .dependency(dependency)
            .build()
    );
    linkRepository.save(
        Link.builder()
            .name("gradle")
            .title("Gradle")
            .url("")
            .dependency(dependency)
            .build()
    );
  }

  /*private void saveSpringType(String springJsonString) {
    try {
      JsonNode nodes = objectMapper.readTree(springJsonString);
      if (!nodes.isArray()) return;

      for (JsonNode node : nodes) {
        DependencyType type = typeRepository.save(DependencyType.builder().name(node.path("name").asText()).languageType(LanguageType.SPRING).build());
        saveSpringDependencies(node.path("dependencies"), type);
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

  }*/
  private void saveSpringDependencies(JsonNode nodes, DependencyType type)
      throws JsonProcessingException {

    if (!nodes.isArray()) {
      return;
    }
    for (JsonNode node : nodes) {
      Dependency dependency = dependencyRepository.save(
          Dependency.builder().id(node.path("id").asText()).name(node.path("name").asText())
              .description(
                  node.path("description").asText()).type(type).notice(node.path("notice").asText())
              .build());

      saveSpringLinks(node.path("links"), dependency);
      saveSpringProperties(node.path("properties"), dependency);
    }


  }

  private void saveSpringProperties(JsonNode nodes, Dependency dependency) {
    if (!nodes.isArray()) {
      return;
    }
    for (JsonNode node : nodes) {
      Property property = propertyRepository.save(
          Property.builder().id(node.path("id").asText())
              .value(node.path("value").asText()).title(
                  node.path("title").asText()).toolTip(node.path("toolTip").asText())
              .dependency(dependency).build());

      saveSpringSelectOptions(node.path("options"), property);
    }
  }

  private void saveSpringSelectOptions(JsonNode nodes, Property property) {
    if (!nodes.isArray()) {
      return;
    }
    for (JsonNode node : nodes) {
      System.out.println(node);
      selectOptionRepository.save(
          SelectOption.builder().label(node.path("label").asText())
              .value(node.path("value").asText()).property(property).build());
    }
  }

  private void saveSpringLinks(JsonNode nodes, Dependency dependency) {

    if (!nodes.isArray()) {
      return;
    }

    for (JsonNode node : nodes) {
      linkRepository.save(
          Link.builder().name(node.path("name").asText()).title(node.path("title").asText()).url(
              node.path("url").asText()).dependency(dependency).build());
    }
  }

  private String getSpringJsonObject(String path) {
    ClassPathResource resource = new ClassPathResource(path);
    Path filePath = null;
    try {
      filePath = Paths.get(resource.getURI());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    try {
      return Files.readString(filePath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
