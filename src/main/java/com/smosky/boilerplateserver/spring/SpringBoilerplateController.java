package com.smosky.boilerplateserver.spring;

import com.smosky.boilerplateserver.shared.AppInfoConfigDto;
import com.smosky.boilerplateserver.shared.Constant;
import com.smosky.boilerplateserver.spring.dtos.*;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
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
      List<String> defaultTypes = Constant.DEFAULT_JAVA_TYPE;

      writer.write(packagePath + ";");
      writer.newLine();
      writer.write("import jakarta.persistence.*;");
      writer.newLine();
      writer.write("import lombok.*;");
      writer.newLine();
      writer.write("import java.util.List;");
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
      writer.write("@Table(name = \"tbl_" + dto.getName().toLowerCase() + "\")");
      writer.newLine();
      /*Class*/
      writer.write("public class " + dto.getName() + "{");
      writer.newLine();

      String id = "";
      for (TemplateDto templateDto : dto.getTemplates()) {
        if (templateDto.getPrimary()) {
          writer.write("@Id");
          writer.newLine();
          writer.write("@GeneratedValue");
          writer.newLine();
          writer.write("private " + templateDto.getType() + " " + templateDto.getName()+";");
          writer.newLine();
          id = templateDto.getName();
        } else if (!defaultTypes.contains(templateDto.getType())) {

          switch (templateDto.getType()) {
            case "OneToOne": {
              if (templateDto.getMappedBy() != null && templateDto.getMappedBy().isEmpty()) {
                writer.write("@" + templateDto.getType() + "(" + "cascade = CascadeType.ALL" + ")");
                writer.newLine();
                writer.write(
                    "@JoinColumn(name = " + "\"" + templateDto.getName().toLowerCase() + "_" + templateDto.getReferencedColumnName() + "\"" + ", referencedColumnName = " + "\"" + templateDto.getReferencedColumnName() + "\"" + ")");
              } else if (templateDto.getMappedBy() != null && templateDto.getReferencedColumnName().isEmpty()) {
                writer.write("@" + templateDto.getType() + "(" + "mappedBy = " + "\"" + templateDto.getMappedBy() + "\"" + ")");
              }


              writer.newLine();
              writer.write("private " + templateDto.getName() + " " + templateDto.getName().toLowerCase() + ";");
              writer.newLine();
              break;
            }
            case "OneToMany": {
              if (!templateDto.getMappedBy().isEmpty()) {
                writer.write("@OneToMany(mappedBy = \""+templateDto.getMappedBy()+"\")");
                writer.newLine();
                writer.write("private " + "List<"+templateDto.getName()+">" + " " + templateDto.getName().toLowerCase() + "s;");
              }
              break;
            }
            case "ManyToOne": {
              if (!templateDto.getReferencedColumnName().isEmpty()) {
                writer.write("@ManyToOne(fetch = FetchType.EAGER)");
                writer.newLine();
                writer.write("@JoinColumn(name=\""+templateDto.getName().toLowerCase()+"_"+templateDto.getReferencedColumnName()+"\")");
                writer.newLine();
                writer.write("private " + templateDto.getName() + " " + templateDto.getName().toLowerCase() + ";");
              }
              break;
            }

            case "ManyToMany": {
              writer.newLine();
              if (templateDto.getMappedBy() != null && templateDto.getMappedBy().isEmpty()) {
              writer.write("@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)");
              writer.newLine();
                writer.write("@EqualsAndHashCode.Exclude");
                writer.newLine();
                writer.write("@ToString.Exclude");
                writer.newLine();
                String joinTableName = dto.getName().toLowerCase() + "_" + templateDto.getName().toLowerCase();
                String joinColumn = dto.getName().toLowerCase() + "_" + id;
                String inverseJoinColumn = templateDto.getName().toLowerCase()+"_" + templateDto.getReferencedColumnName();
                writer.write("@JoinTable(name = \""+joinTableName+"\",\n" +
                    "                    joinColumns = @JoinColumn(name = \""+joinColumn+"\"),\n" +
                    "                    inverseJoinColumns = @JoinColumn(name = \""+inverseJoinColumn+"\")\n" +
                    "                )");
              }else if (templateDto.getMappedBy() != null && templateDto.getReferencedColumnName().isEmpty()) {
                writer.write("@ManyToMany(mappedBy = \""+templateDto.getMappedBy()+"\")");
                writer.newLine();
                writer.write("@EqualsAndHashCode.Exclude");
                writer.newLine();
                writer.write("@ToString.Exclude");
              }
              writer.newLine();
              writer.write("private List<"+templateDto.getName()+">"+" "+" "+templateDto.getName().toLowerCase()+"s;");
              break;
            }


            default:
              break;
          }

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
      e.printStackTrace();
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
