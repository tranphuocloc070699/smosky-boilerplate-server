package com.smosky.boilerplateserver.spring;

import com.smosky.boilerplateserver.shared.AppInfoConfigDto;
import com.smosky.boilerplateserver.spring.dtos.*;
import lombok.RequiredArgsConstructor;
import net.lingala.zip4j.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/spring")
@RequiredArgsConstructor
public class SpringBoilerplateController {

  private final DependencyRepository repository;
  private final PropertyRepository propertyRepository;
  private final SelectOptionRepository selectOptionRepository;
  private final LinkRepository linkRepository;
  private final TypeRepository typeRepository;
  private final AppInfoConfigDto appInfoConfigDto;
  public static final String CORRELATION_ID = "X-CORRELATION-ID";
  final int size = 100 * 1024 * 1024;
  final ExchangeStrategies strategies = ExchangeStrategies.builder()
      .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
      .build();

  private static final Logger logger = LoggerFactory.getLogger(SpringBoilerplateController.class);
  @GetMapping("")
  public Object hello() {
    String[] array = new String[]{"/home/loctran/.nvm/versions/node/v21.2.0/bin/npm","init","-y"};
    try {
      Process process = new ProcessBuilder(array).redirectErrorStream(true).start();

      InputStream is = process.getInputStream();
      InputStream error = process.getErrorStream();
      process.getOutputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      BufferedReader errorReader = new BufferedReader(new InputStreamReader(error));


      String line;
      StringBuilder output = new StringBuilder();
//      while ((line = reader.readLine()) != null) {
//        output.append(line).append("\n");
//        System.out.println(line);
//      }

      List<String> arr = readOutput(is);
      for (var item : arr) {
        System.out.println(item);
        output.append(item).append("\n");
      }
      return output;
    } catch (IOException e) {
      return e.getMessage();
    }
  }

 /* @PostMapping("")
  public Object boilerplate(@RequestBody Dependency dependency) {
    String randomName= UUID.randomUUID().toString();
    String apiUrl = "https://start.spring.io/starter.zip";
    String queryParams = "bootVersion=3.2.1&type=maven-project&packaging=jar&jvmVersion=17" +
        "&groupId=com.loctran&artifactId=demo&name=demo&description=description" +
        "&dependencies=lombok,web,data-jpa,postgresql";

    String urlString = apiUrl + "?" + queryParams;
    WebClient webClient = WebClient.create();
    byte[] responseBody = webClient.get()
        .uri(urlString)
        .retrieve()
        .bodyToMono(byte[].class)
        .block();

    if (responseBody != null) {
      // Save the response to a file (my-project.zip)
      try {
        *//* Write .zip file from start.spring.io to folder *//*
        Files.write(Path.of("my-project"+".zip"), responseBody);

        *//* Extract .zip file *//*
        ZipFile zipFile = new ZipFile("my-project.zip");
        zipFile.extractAll("extract-project");

        *//*
        * Validate value
   * *//*

   *//* Write config to application.properties *//*
        File file = new File("extract-project/src/main/resources/application.properties");
//        writeArrayListToFile(dependency.getProperties(),file.toPath().toString());

        System.out.println("file existed:" + file);
        return file.exists();


      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("Failed to download.");
    }
    return true;
  }*/

  @PostMapping("")
  public Object boilerplate(@RequestBody CreateBoilerplateDto dto) {
    String randomName = UUID.randomUUID().toString();
    StringBuilder dependencies = new StringBuilder("lombok");

    List<DependencyDto> dependencyDtos = dto.getDependencies();
    for (DependencyDto dependencyDto : dependencyDtos) {
      dependencies.append(",").append(dependencyDto.getId());
    }
//    System.out.println(dependencies);
    String urlString = getString(dto, dependencies);
        WebClient webClient = WebClient.create();
    byte[] responseBody = webClient.get()
        .uri(urlString)
        .retrieve()
        .bodyToMono(byte[].class)
        .block();
//    return urlString;
//    String queryParams = "bootVersion=3.2.1&type=maven-project&packaging=jar&jvmVersion=17" +
//        "&groupId=com.loctran&artifactId=demo&name=demo&description=description" +
//        "&dependencies=lombok,web,data-jpa,postgresql";
//



    if (responseBody != null) {
      // Save the response to a file (my-project.zip)
      try {
        /* Write .zip file from start.spring.io to folder */
        Files.write(Path.of("my-project"+".zip"), responseBody);

        /* Extract .zip file */
        ZipFile zipFile = new ZipFile("my-project.zip");
        zipFile.extractAll("extract-project");

        /*
         * Validate value
         * */

        /* Write config to application.properties */
        File file = new File("extract-project/src/main/resources/application.properties");
        writeArrayListToFile(dto.getDependencies(),file.toPath().toString());
        for (EntityDto entityDto : dto.getEntities()) {
          String entitiesPath = "extract-project/src/main/java/" + dto.getMetadata().getGroupId().replace(".","/") + "/" + dto.getMetadata().getArtifactId() + "/entities";
          File entitiesFolder = new File(entitiesPath);
          entitiesFolder.mkdir();
          String packagePath = "package " + dto.getMetadata().getGroupId() + "." + dto.getMetadata().getArtifactId() + ".entities";
          String entityPath = entitiesPath+"/"+entityDto.getName();
          createEntityTemplate(entityDto,packagePath,entityPath);
        }


        return file.exists();


      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("Failed to download.");
    }
  return true;
  }

  private String getString(CreateBoilerplateDto dto, StringBuilder dependencies) {
    MetadataDto metadataDto = dto.getMetadata();
    String apiUrl = "https://start.spring.io/starter.zip";
    String queryParams = String.format("bootVersion=%s&type=%s&packaging=%s&jvmVersion=%d" +
            "&groupId=%s&artifactId=%s&name=%s&description=%s" +
            "&dependencies=%s", dto.getBootVersion(), dto.getType(), metadataDto.getPackaging(), metadataDto.getJvmVersion(),
        metadataDto.getGroupId(), metadataDto.getArtifactId(), metadataDto.getName(),metadataDto.getDescription(), dependencies);
    String urlString = apiUrl + "?" + queryParams;
    return urlString;
  }

  private static void writeArrayListToFile(List<DependencyDto> arrayList, String filePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      for (DependencyDto line : arrayList) {
        writer.newLine();
        writer.write("# "+ line.getId());
        writer.newLine();
        for (PropertyDto propertyDto : line.getProperties()) {
          writer.write(propertyDto.getId()+"="+propertyDto.getDefaultValue());
          writer.newLine();
        }
        // Write each line followed by a newline character

      }
    } catch (IOException e) {
      e.printStackTrace(); // Handle the exception based on your requirements
    }
  }

  private static void createEntityTemplate(EntityDto dto, String packagePath, String filePath) {
    File file = new File(filePath+".java");
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath+".java"))) {
      ArrayList<String> defaultTypes = new ArrayList<>(List.of("String", "Integer", "Float"));

      writer.write(packagePath + ";");
      writer.newLine();
      writer.write("import jakarta.persistence.*;");
      writer.newLine();
      writer.write("import lombok.AllArgsConstructor;");
      writer.newLine();
      writer.write("import lombok.Builder;");
      writer.newLine();
      writer.write("import lombok.Data;");
      writer.newLine();
      writer.write("import lombok.NoArgsConstructor;\n");
      writer.newLine();

      writer.write("@Data");
      writer.newLine();
      writer.write("@NoArgsConstructor");
      writer.newLine();
      writer.write("@AllArgsConstructor");
      writer.newLine();
      writer.write("@Builder");
      writer.newLine();
      writer.write("@Entity");
      writer.newLine();

      /*Class*/
      writer.write("public class " + dto.getName() + "{");
      writer.newLine();
      for (TemplateDto templateDto : dto.getTemplates()) {
        if (templateDto.getPrimary()) {
          writer.write("@Id");
          writer.newLine();
          writer.write("@GeneratedValue");
          writer.newLine();
          writer.write("private " + templateDto.getType() + " " + templateDto.getName()+";");
          writer.newLine();
        } else if (!defaultTypes.contains(templateDto.getType())) {

        } else {
          writer.write("@Column");
          writer.newLine();
          writer.write("private " + templateDto.getType() + " " + templateDto.getName() +";");
          writer.newLine();
        }

        writer.newLine();
      }
      writer.newLine();
      writer.write("}");

    /*  for (DependencyDto line : arrayList) {
        writer.newLine();
        writer.write("# "+ line.getId());
        writer.newLine();
        for (PropertyDto propertyDto : line.getProperties()) {
          writer.write(propertyDto.getId()+"="+propertyDto.getDefaultValue());
          writer.newLine();
        }
        // Write each line followed by a newline character

      }*/
    } catch (IOException e) {
      e.printStackTrace(); // Handle the exception based on your requirements
    }
  }

  @PostMapping("/dependency")
  public Object createDependency(@RequestBody Dependency dto) {

    return repository.save(dto);
  }

  @GetMapping("/ci-cd")
  public Object getCiCd(  @RequestHeader(CORRELATION_ID)
                            String correlationId){
    System.out.println(String.format(CORRELATION_ID + " found: {} ", correlationId));
    List<DependencyType> types = typeRepository.findAllWithDependencies();
    return types;
  }


  private List<String> readOutput(InputStream inputStream) throws IOException {
    try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
      return output.lines()
          .collect(Collectors.toList());

    }
  }

  @GetMapping("/contact-info")
  public ResponseEntity<Object> getContactInfo() {
    System.out.println(appInfoConfigDto.getMessage());
    return ResponseEntity.status(HttpStatus.OK).body(appInfoConfigDto);
  }
}
