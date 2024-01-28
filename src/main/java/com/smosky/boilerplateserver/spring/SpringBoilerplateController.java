package com.smosky.boilerplateserver.spring;

import com.smosky.boilerplateserver.shared.AppInfoConfigDto;
import com.smosky.boilerplateserver.shared.Constant;
import com.smosky.boilerplateserver.spring.dtos.CreateBoilerplateDto;
import com.smosky.boilerplateserver.spring.dtos.CreateEntityDependencyId;
import com.smosky.boilerplateserver.spring.dtos.DependencyDto;
import com.smosky.boilerplateserver.spring.dtos.EntityDto;
import com.smosky.boilerplateserver.spring.dtos.MetadataDto;
import com.smosky.boilerplateserver.spring.dtos.TemplateDto;
import com.smosky.boilerplateserver.util.ApplicationFileTemplate;
import com.smosky.boilerplateserver.util.CustomWriter;
import com.smosky.boilerplateserver.util.DaoTemplate;
import com.smosky.boilerplateserver.util.DtoTemplate;
import com.smosky.boilerplateserver.util.EntityTemplate;
import com.smosky.boilerplateserver.util.ExceptionTemplate;
import com.smosky.boilerplateserver.util.MapperTemplate;
import com.smosky.boilerplateserver.util.RepositoryTemplate;
import com.smosky.boilerplateserver.util.ServiceTemplate;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;


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
  private final List<String> defaultTypes = Constant.DEFAULT_JAVA_TYPE;
  public static final String CORRELATION_ID = "X-CORRELATION-ID";
  final int size = 100 * 1024 * 1024;
  final ExchangeStrategies strategies = ExchangeStrategies.builder()
      .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
      .build();

  private static final Logger logger = LoggerFactory.getLogger(SpringBoilerplateController.class);

  @GetMapping("")
  public Object hello() {
    String[] array = new String[]{"/home/loctran/.nvm/versions/node/v21.2.0/bin/npm", "init", "-y"};
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
        Files.write(Path.of("my-project" + ".zip"), responseBody);

        /* Extract .zip file */
        ZipFile zipFile = new ZipFile("my-project.zip");
        zipFile.extractAll("extract-project");

        /*
         * Validate value
         * */

        /* Write config to application.properties */
        File file = new File("extract-project/src/main/resources/application.properties");
        ApplicationFileTemplate.writeArrayListToFile(dto.getDependencies(),
            file.toPath().toString());

        /*Package*/
        String packagePath =
            "extract-project/src/main/java/" + dto.getMetadata().getGroupId().replace(".", "/")
                + "/" + dto.getMetadata().getArtifactId();

        String packageName =
            dto.getMetadata().getGroupId() + "." + dto.getMetadata().getArtifactId();

        /*Dto*/
        String dtoFolderPath = packagePath + "/dto";
        File dtoFolder = new File(dtoFolderPath);
        dtoFolder.mkdir();

        String dtoPackagePath = "package " + packageName + ".dto";
        String responseDtoFilePath = dtoFolderPath + "/ResponseDto.java";

        /*Response Dto*/
        DtoTemplate.createResponseDto(dtoPackagePath, responseDtoFilePath);

        /*Mapper*/
        String mapperFolderPath = packagePath + "/mapper";
        File mapperFolder = new File(mapperFolderPath);
        mapperFolder.mkdir();

        /*Repository*/
        String repositoryFolderPath = packagePath + "/repository";
        File repositoryFolder = new File(repositoryFolderPath);
        repositoryFolder.mkdir();

        /*Dao*/
        String daoFolderPath = packagePath + "/dao";
        File daoFolder = new File(daoFolderPath);
        daoFolder.mkdir();

        /*Service*/
        String serviceFolderPath = packagePath + "/service";
        File serviceFolder = new File(serviceFolderPath);
        serviceFolder.mkdir();

        /*Controller*/
        String controllerFolderPath = packagePath + "/controller";
        File controllerFolder = new File(controllerFolderPath);
        controllerFolder.mkdir();

        for (EntityDto entityDto : dto.getEntities()) {
          /*Entity */
          String entitiesPath = packagePath + "/entity";
          File entitiesFolder = new File(entitiesPath);
          entitiesFolder.mkdir();
          String packageEntityName = "package " + packageName + ".entity";
          String entityPath = entitiesPath + "/" + entityDto.getName() + ".java";
          EntityTemplate.createEntityTemplate(entityDto, packageEntityName, entityPath);
          /*Dto*/
          String dtoFilePath = dtoFolderPath + "/" + entityDto.getName() + "Dto" + ".java";
          DtoTemplate.createDto(entityDto, dtoPackagePath, dtoFilePath);
          /*Mapper*/
          String mapperFilePath = mapperFolderPath + "/" + entityDto.getName() + "Mapper" + ".java";
          String mapperPackagePath = "package " + packageName + ".mapper";
          MapperTemplate.createMapperFile(packageName, entityDto, mapperPackagePath,
              mapperFilePath);

          /*Repository*/
          String repositoryFilePath =
              repositoryFolderPath + "/" + entityDto.getName() + "Repository" + ".java";
          String repositoryPackagePath = "package " + packageName + ".repository";
          RepositoryTemplate.createRepositoryFile(packageName, entityDto, repositoryFilePath,
              repositoryPackagePath);
          /*Dao*/
          String daoFilePath = daoFolderPath + "/" + entityDto.getName() + "DataAccess" + ".java";
          String daoPackagePath = "package " + packageName + ".dao";
          DaoTemplate.createDaoFile(packageName, entityDto, daoFilePath, daoPackagePath);

          /*Service*/
          String serviceFilePath =
              serviceFolderPath + "/" + entityDto.getName() + "Service" + ".java";
          String servicePackagePath = "package " + packageName + ".service";
          ServiceTemplate.createServiceFile(packageName, entityDto, serviceFilePath,
              servicePackagePath);

          /*Controller*/
          String controllerFilePath =
              controllerFolderPath + "/" + entityDto.getName() + "Controller" + ".java";
          String controllerPackagePath = "package " + packageName + ".controller";
          createControllerFile(packageName, entityDto, controllerFilePath, controllerPackagePath);
        }
        /*Exception*/
        File exceptionSource = new File("shared/exception");
        File exceptionDir = new File(packagePath + "/exception");
        FileUtils.copyDirectory(exceptionSource, exceptionDir);

        String exceptionPackagePath = "package " + packageName + ".exception;";
        String responseDtoPath = packageName + ".dto" + ".ResponseDto";
        ExceptionTemplate.writePackageToExceptionFile(exceptionDir.getPath(), exceptionPackagePath,
            responseDtoPath);

        return file.exists();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("Failed to download.");
    }
    return true;
  }

  public static void createControllerFile(String packageName, EntityDto entityDto,
      String controllerFilePath, String controllerPackagePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(controllerFilePath))) {
      CustomWriter customWriter = new CustomWriter(writer);
      /*Package*/
      customWriter.write(2, controllerPackagePath + ";");
      /*Import file*/
      customWriter.write("import ", packageName, ".entity.", entityDto.getName(), ";");
      customWriter.write("import ", packageName, ".service.", entityDto.getName(), "Service", ";");
      customWriter.write("import ", packageName, ".dto.", entityDto.getName(), "Dto", ";");
      customWriter.write(2, "import ", packageName, ".dto.", "ResponseDto", ";");
      customWriter.write("import jakarta.servlet.http.HttpServletRequest;");
      customWriter.write("import lombok.RequiredArgsConstructor;");
      customWriter.write("import org.springframework.data.domain.Page;");
      customWriter.write("import org.springframework.http.HttpStatus;");
      customWriter.write("import org.springframework.http.ResponseEntity;");
      customWriter.write("import org.springframework.web.bind.annotation.*;");
      customWriter.write(2, "import java.util.Date;");
      /*Class*/
      customWriter.write("@RestController");
      customWriter.write("@RequiredArgsConstructor");
      customWriter.write("@RequestMapping(\"api/", entityDto.getName().toLowerCase(), "s", "\")");
      customWriter.write(2, "public class ", entityDto.getName(), "Controller {");
      customWriter.write("private final ", entityDto.getName(), "Service service;");
      customWriter.write(2, "private final HttpServletRequest httpServletRequest;");

      /*Start fetchEntities*/
      customWriter.write("\t@GetMapping(\"\")");
      customWriter.write(
          "\t public Object fetchEntities(@RequestParam(value = \"current_page\", defaultValue = \"1\", required = false) int currentPage,");
      customWriter.write(
          "\t @RequestParam(value = \"page_size\", defaultValue = \"10\") int pageSize,");
      customWriter.write(
          "\t @RequestParam(value = \"sort_by\", defaultValue = \"id\") String sortBy,");
      customWriter.write(2,
          "\t @RequestParam(value = \"sort_dir\", defaultValue = \"desc\", required = false) String sortDir) {");
      customWriter.write("\t \t \tPage<", entityDto.getName(),
          "> entities = service.fetchEntities(currentPage, pageSize, sortBy, sortDir);");
      customWriter.write("\t \t \tResponseDto responseDto = ResponseDto.builder()");
      customWriter.write("\t \t \t.timestamp(new Date())");
      customWriter.write("\t \t \t.status(HttpStatus.CREATED)");
      customWriter.write("\t \t \t.path(httpServletRequest.getContextPath())");
      customWriter.write("\t \t \t.data(entities)");
      customWriter.write("\t \t \t.errors(null)");
      customWriter.write("\t \t \t.message(\"Process Successfully!\")");
      customWriter.write("\t \t \t.build();");
      customWriter.write("\t \t \treturn ResponseEntity.status(HttpStatus.OK).body(responseDto);");
      customWriter.write("\t \t}");
      /*End fetchEntities*/

      /*Start fetchDetails*/
      customWriter.write("\t@GetMapping(\"{id}\")");
      customWriter.write(
          "\t public Object fetchDetails(@RequestParam(value = \"id\") Integer id) {");
      customWriter.write("\t \t", entityDto.getName(), " entity = service.fetchEntity(id);");
      customWriter.write("\t \t \tResponseDto responseDto = ResponseDto.builder()");
      customWriter.write("\t \t \t.timestamp(new Date())");
      customWriter.write("\t \t \t.status(HttpStatus.CREATED)");
      customWriter.write("\t \t \t.path(httpServletRequest.getContextPath())");
      customWriter.write("\t \t \t.data(entity)");
      customWriter.write("\t \t \t.errors(null)");
      customWriter.write("\t \t \t.message(\"Process Successfully!\")");
      customWriter.write("\t \t \t.build();");
      customWriter.write("\t \t \treturn ResponseEntity.status(HttpStatus.OK).body(responseDto);");
      customWriter.write("\t \t}");
      /*End fetchDetails*/

      /*Start createEntity*/
      String createEntityDependencyParams = "";
      String createEntityDependencyIds = "";
      List<CreateEntityDependencyId> entityDependencyIds = new ArrayList<>();
      for (TemplateDto templateDto : entityDto.getTemplates()) {
        if ((templateDto.getType().equals("OneToOne") && !templateDto.getMappedBy().isEmpty()) || (
            templateDto.getType().equals("ManyToOne") && !templateDto.getReferencedColumnName()
                .isEmpty())) {
          createEntityDependencyParams +=
              "@RequestParam(\"" + templateDto.getName().toLowerCase() + "Id\") Integer "
                  + templateDto.getName().toLowerCase() + "Id,";

          createEntityDependencyIds += templateDto.getName().toLowerCase() + "Id,";
          entityDependencyIds.add(
              CreateEntityDependencyId.builder().id(templateDto.getName().toLowerCase() + "Id")
                  .name(templateDto.getName()).build());
        }
      }

      customWriter.write("\t@PostMapping(\"\")");
      customWriter.write("\t public Object createEntity(", createEntityDependencyParams,
          "@RequestBody ", entityDto.getName(), "Dto dto) {");
      customWriter.write("\t \t", entityDto.getName(), " entity = service.createEntity(",
          createEntityDependencyIds, "dto);");
      customWriter.write("\t \t \tResponseDto responseDto = ResponseDto.builder()");
      customWriter.write("\t \t \t.timestamp(new Date())");
      customWriter.write("\t \t \t.status(HttpStatus.CREATED)");
      customWriter.write("\t \t \t.path(httpServletRequest.getContextPath())");
      customWriter.write("\t \t \t.data(entity)");
      customWriter.write("\t \t \t.errors(null)");
      customWriter.write("\t \t \t.message(\"Process Successfully!\")");
      customWriter.write("\t \t \t.build();");
      customWriter.write("\t \t \treturn ResponseEntity.status(HttpStatus.OK).body(responseDto);");
      customWriter.write("\t \t}");
      /*End createEntity*/

      /*Start updateEntity*/
      customWriter.write("\t@PutMapping(\"{id}\")");
      customWriter.write(
          "\t public Object updateEntity(@PathVariable(\"id\")Integer id,@RequestBody ",
          entityDto.getName(), "Dto dto) {");
      customWriter.write("\t \t", entityDto.getName(), " entity = service.updateEntity(dto,id);");
      customWriter.write("\t \t \tResponseDto responseDto = ResponseDto.builder()");
      customWriter.write("\t \t \t.timestamp(new Date())");
      customWriter.write("\t \t \t.status(HttpStatus.OK)");
      customWriter.write("\t \t \t.path(httpServletRequest.getContextPath())");
      customWriter.write("\t \t \t.data(entity)");
      customWriter.write("\t \t \t.errors(null)");
      customWriter.write("\t \t \t.message(\"Process Successfully!\")");
      customWriter.write("\t \t \t.build();");
      customWriter.write("\t \t \treturn ResponseEntity.status(HttpStatus.OK).body(responseDto);");
      customWriter.write("\t \t}");
      /*End updateEntity*/

      /*Start deleteEntity*/
      customWriter.write("\t@DeleteMapping(\"{id}\")");
      customWriter.write("\t public Object deleteEntity(@PathVariable(\"id\")Integer id) {");
      customWriter.write("\t \t", entityDto.getName(), " entity = service.deleteEntity(id);");
      customWriter.write("\t \t \tResponseDto responseDto = ResponseDto.builder()");
      customWriter.write("\t \t \t.timestamp(new Date())");
      customWriter.write("\t \t \t.status(HttpStatus.OK)");
      customWriter.write("\t \t \t.path(httpServletRequest.getContextPath())");
      customWriter.write("\t \t \t.data(entity)");
      customWriter.write("\t \t \t.errors(null)");
      customWriter.write("\t \t \t.message(\"Process Successfully!\")");
      customWriter.write("\t \t \t.build();");
      customWriter.write("\t \t \treturn ResponseEntity.status(HttpStatus.OK).body(responseDto);");
      customWriter.write("\t \t}");
      /*End deleteEntity*/

      for (TemplateDto templateDto : entityDto.getTemplates()) {
        if (templateDto.getType().equals("ManyToMany") && !templateDto.getReferencedColumnName()
            .isEmpty()) {
          /*Start approveEntity*/
          customWriter.write("\t@PatchMapping(\"/add/{id}\")");
          customWriter.write("\t public Object add", templateDto.getName(),
              "(@PathVariable(\"id\")Integer id,@RequestParam(\"",
              templateDto.getName().toLowerCase(), "Id\") String ",
              templateDto.getName().toLowerCase(), "Id) {");
          customWriter.write("\t \t", entityDto.getName(), " entity = service.add",
              templateDto.getName(), "(id,Integer.parseInt(", templateDto.getName().toLowerCase(),
              "Id));");
          customWriter.write("\t \t \tResponseDto responseDto = ResponseDto.builder()");
          customWriter.write("\t \t \t.timestamp(new Date())");
          customWriter.write("\t \t \t.status(HttpStatus.OK)");
          customWriter.write("\t \t \t.path(httpServletRequest.getContextPath())");
          customWriter.write("\t \t \t.data(entity)");
          customWriter.write("\t \t \t.errors(null)");
          customWriter.write("\t \t \t.message(\"Process Successfully!\")");
          customWriter.write("\t \t \t.build();");
          customWriter.write(
              "\t \t \treturn ResponseEntity.status(HttpStatus.OK).body(responseDto);");
          customWriter.write("\t \t}");
          /*End approveEntity*/

          /*Start removeEntity*/
          customWriter.write("\t@PatchMapping(\"/remove/{id}\")");
          customWriter.write("\t public Object remove", templateDto.getName(),
              "(@PathVariable(\"id\")Integer id,@RequestParam(\"",
              templateDto.getName().toLowerCase(), "Id\") String ",
              templateDto.getName().toLowerCase(), "Id) {");
          customWriter.write("\t \t", entityDto.getName(), " entity = service.remove",
              templateDto.getName(), "(id,Integer.parseInt(", templateDto.getName().toLowerCase(),
              "Id));");
          customWriter.write("\t \t \tResponseDto responseDto = ResponseDto.builder()");
          customWriter.write("\t \t \t.timestamp(new Date())");
          customWriter.write("\t \t \t.status(HttpStatus.OK)");
          customWriter.write("\t \t \t.path(httpServletRequest.getContextPath())");
          customWriter.write("\t \t \t.data(entity)");
          customWriter.write("\t \t \t.errors(null)");
          customWriter.write("\t \t \t.message(\"Process Successfully!\")");
          customWriter.write("\t \t \t.build();");
          customWriter.write(
              "\t \t \treturn ResponseEntity.status(HttpStatus.OK).body(responseDto);");
          customWriter.write("\t \t}");
          /*End removeEntity*/
        }
      }

      /*End class*/
      customWriter.write("}");


    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  private String getString(CreateBoilerplateDto dto, StringBuilder dependencies) {
    MetadataDto metadataDto = dto.getMetadata();
    String apiUrl = "https://start.spring.io/starter.zip";
    String queryParams = String.format("bootVersion=%s&type=%s&packaging=%s&jvmVersion=%d"
            + "&groupId=%s&artifactId=%s&name=%s&description=%s" + "&dependencies=%s",
        dto.getBootVersion(), dto.getType(), metadataDto.getPackaging(),
        metadataDto.getJvmVersion(), metadataDto.getGroupId(), metadataDto.getArtifactId(),
        metadataDto.getName(), metadataDto.getDescription(), dependencies);
    String urlString = apiUrl + "?" + queryParams;
    return urlString;
  }


  @PostMapping("/dependency")
  public Object createDependency(@RequestBody Dependency dto) {

    return repository.save(dto);
  }

  @GetMapping("/ci-cd")
  public Object getCiCd(@RequestHeader(CORRELATION_ID) String correlationId) {
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
