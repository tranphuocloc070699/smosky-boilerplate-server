package com.smosky.boilerplateserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smosky.boilerplateserver.database.DataStorage;
import com.smosky.boilerplateserver.spring.*;
import java.util.Optional;

import com.smosky.boilerplateserver.spring.entity.Boilerplate;
import com.smosky.boilerplateserver.spring.entity.Tag;
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
import java.util.List;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.util.Random;

@SpringBootApplication
@RequiredArgsConstructor
@EnableScheduling
public class BoilerplateServerApplication implements CommandLineRunner {
  private final BoilerplateRepository boilerplateRepository;
  private final TagRepository tagRepository;
  private final DataStorage dataStorage;
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static void main(String[] args) {
    SpringApplication.run(BoilerplateServerApplication.class, args);
  }


  @Override
  public void run(String... args) throws Exception {
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

      if (node.path("tags").isArray()) {
        for (JsonNode tag : node.path("tags")) {
          Optional<Tag> tagOptional = tagRepository.findByName(tag.asText());
          if (tagOptional.isEmpty()) {
            tagRepository.save(Tag.builder().name(tag.asText()).build());
          }
        }
      }

      boilerplateRepository.save(Boilerplate.builder()
          .name(node.path("name").asText())
          .description(node.path("description").asText())
          .dependencies(dependencies)
          .features(features)
          .previewLink(node.path("previewLink").asText())
          .thumbnail(node.path("thumbnail").asText())
          .build());

    }
  }

  private void convertOriginal() throws JsonProcessingException {
    String jsonString = getSpringJsonObject("original.json");
    JsonNode nodes = objectMapper.readTree(jsonString).path("dependencies");
    for (JsonNode node : nodes) {
      DependencyType type = DependencyType.builder()
          .id(dataStorage.getDependencyTypes().size() + 1)
          .name(node.path("name").asText())
          .languageType(LanguageType.SPRING)
          .build();
      dataStorage.pushDependencyTypeToList(type);

      JsonNode dependenciesNodes = node.path("content");
      convertDependencyOriginal(dependenciesNodes, type);
    }
  }

  private void convertDependencyOriginal(JsonNode dependenciesNodes, DependencyType type) {
    List<Dependency> dependencies = new ArrayList<>();
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
     /* Dependency dependency = dependencyRepository.save(

      );*/

      Dependency dependency = Dependency.builder()
          .id(dependencyNode.path("id").asText())
          .name(dependencyNode.path("name").asText())
          .description(dependencyNode.path("description").asText())
          .compatibilityRanges(Arrays.stream(splitResult).toList())
          .type(type)
          .build();
      dependencies.add(dependency);
      dataStorage.pushDependencyToList(dependency);

      JsonNode propertiesNode = dependencyNode.path("properties");
      JsonNode linksNode = dependencyNode.path("links");
      convertLinksOriginal(linksNode, dependency);
      convertPropertiesAndOptionsOriginal(propertiesNode, dependency);
    }
    type.setDependencies(dependencies);
  }

  private void convertPropertiesAndOptionsOriginal(JsonNode propertiesNode, Dependency dependency) {

    if (propertiesNode == null || !propertiesNode.isArray()) {
      return;
    }
    List<Property> properties = new ArrayList<>();
    for (JsonNode node : propertiesNode) {

      Boolean disabled = node.path("required") != null && node.path("required").asBoolean();
      List<SelectOption> selectOptions = new ArrayList<>();
      if (node.path("options") != null && node.path("options").isArray()) {
        for (JsonNode optionNode : node.path("options")) {
          Random random = new Random();
          int randomNumber = random.nextInt(10001) + properties.size();
          selectOptions.add(SelectOption.builder()
              .id(randomNumber)
              .label(optionNode.path("label").asText())
              .value(optionNode.path("value").asText())
              .build());
        }
      }
      properties.add(Property.builder()
          .id(node.path("id").asText())
          .title(node.path("title").asText())
          .value(node.path("value").asText())
          .toolTip(node.path("toolTip").asText())
          .isDisable(disabled)
          .options(selectOptions)
          .build());
    }
    dependency.setProperties(properties);
  }

  private void convertLinksOriginal(JsonNode linksNode, Dependency dependency) {
    if (linksNode == null || !linksNode.isArray()) {
      return;
    }

    List<Link> links = new ArrayList<>();

    for (JsonNode node : linksNode) {
      links.add(Link.builder()
              .name(node.path("rel").asText())
              .url(node.path("href").asText())
          .build());
    }
    dependency.setLinks(links);
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
