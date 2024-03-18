package com.smosky.boilerplateserver.spring.jpasql.service;

import com.smosky.boilerplateserver.database.DataStorage;
import com.smosky.boilerplateserver.exception.ConflictException;
import com.smosky.boilerplateserver.shared.FileService;
import com.smosky.boilerplateserver.shared.ResponseDto;
import com.smosky.boilerplateserver.spring.BoilerplateRepository;
import com.smosky.boilerplateserver.spring.ReviewRepository;
import com.smosky.boilerplateserver.spring.entity.Tag;
import com.smosky.boilerplateserver.spring.TagRepository;
import com.smosky.boilerplateserver.spring.dtos.*;
import com.smosky.boilerplateserver.util.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SpringBoilerplateService {
  
  private final HttpServletRequest httpServletRequest;
  private final BoilerplateRepository boilerplateRepository;
  private final TagRepository tagRepository;
  private final ReviewRepository reviewRepository;
  private final FileService fileService;
  private final DataStorage dataStorage;
  
  public ResponseEntity<ResponseDto> fetchAllBoilerplate()  {
    try {
      var boilerplateData = boilerplateRepository.findAllWithStarCounting();
      List<Tag> tags = tagRepository.findAll();
      Map<String, Object> map = new HashMap<>();
      map.put("boilerplates", boilerplateData);
      map.put("tags", tags);
      
      ResponseDto responseDto = ResponseDto.builder()
              .path(httpServletRequest.getServletPath())
              .status(HttpStatus.OK.value())
              .message("fetch all boilerplate successfully!")
              .data(map)
              .build();
      
      return ResponseEntity.ok(responseDto);
    } catch (Exception exception) {
        throw new RuntimeException(exception.getMessage());
    }
  }
  
  public ResponseEntity<ResponseDto> fetchBoilerplateDetail(String name) {
    try {
      BoilerplateDetailDto dto = boilerplateRepository.findByName(name);
      dto.setReviews(reviewRepository.findAllByBoilerplateId(dto.getId()));
      dto.setProjectStructure(fileService.getNode(new File("examples/spring-data-jpa-postgresql")));
      
      dto.setDependencies(dataStorage.getDependencyTypes());
      
      dto.setDependenciesSelected(
              convertDuplicatedStringToList(dto.getDependenciesSelected().toString()));
      dto.setFeatures(convertDuplicatedStringToList(dto.getFeatures().toString()));
      
      ResponseDto responseDto = ResponseDto.builder()
              .path(httpServletRequest.getServletPath())
              .status(HttpStatus.OK.value())
              .message("fetch boilerplate successfully!")
              .data(dto)
              .build();
      return ResponseEntity.ok(responseDto);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  private List<String> convertDuplicatedStringToList(String input) {
    String[] splitStrings = input.split(", ");
    
    // Step 2: Convert the array of split strings into a list
    List<String> listOfStrings = Arrays.asList(splitStrings);
    
    // Step 3: Use a Set to remove duplicates
    Set<String> setOfStrings = new HashSet<>(listOfStrings);
    
    // Step 4: Convert the Set back to a list if needed
    List<String> uniqueList = setOfStrings.stream().toList();
    
    return uniqueList;
  }
  
  public Object downloadBoilerplateFromUrl(DownloadPreviewRequestDto dto) {
    InputStreamResource resource = null;
    try {
      System.out.println("Url:" + dto.getDownloadUrl());
      resource = new InputStreamResource(
              new FileInputStream(dto.getDownloadUrl()));
      
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
      headers.setContentDispositionFormData("attachment", "file.zip");
      InputStream inputStream = new ByteArrayInputStream(
              resource.getInputStream().readAllBytes());
      
      
      
      return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
    } catch (IOException | RuntimeException e) {
      System.out.println(e.getMessage());
      throw new RuntimeException(e);
    }
  }
  

  
  
  public ResponseEntity downloadBoilerplate(CreateBoilerplateDto dto) {
    String randomName = dto.getMetadata().getName() + "-" + UUID.randomUUID();
    String zipFileName = "zip/" + randomName + ".zip";
    String zipFileResponse = "zip-response/" + randomName + ".zip";
    String extractFileName = "extract-zip" + "/" + randomName;
    File newFolder = new File(extractFileName);
    if (!newFolder.exists()) {
      newFolder.mkdir();
    }
    
    byte[] responseBody = downloadTemplateFromStartSpringIo(dto);
    
    if (responseBody != null) {
      // Save the response to a file (my-project.zip)
      try {
        
        ZipFile zipFolderToFile = getFile(dto, zipFileName, responseBody,
                extractFileName, zipFileResponse);
        InputStreamResource resource = new InputStreamResource(
                new FileInputStream(zipFileResponse));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", zipFolderToFile.getFile().getName());
        InputStream inputStream = new ByteArrayInputStream(
                resource.getInputStream().readAllBytes());
        return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
        
      } catch (IOException e) {
        throw new RuntimeException(e.getMessage());
      }
    } else {
      
      throw new RuntimeException("Cannot download spring template from start.spring.io");
    }
  }
  
  public Object previewBoilerplate(CreateBoilerplateDto dto) {

    try {
      String randomName = dto.getMetadata().getName() + "-" + UUID.randomUUID();
      String zipFileName = "zip/" + randomName + ".zip";
      String zipFileResponse = "zip-response/" + randomName + ".zip";
      String extractFileName = "extract-zip" + "/" + randomName;
      File newFolder = new File(extractFileName);
      if (!newFolder.exists()) {
        newFolder.mkdir();
      }
      byte[] responseBody = downloadTemplateFromStartSpringIo(dto);
      if (responseBody != null) {
        // Save the response to a file (my-project.zip)
          /* Write .zip file from start.spring.io to folder */

          getFile(dto, zipFileName, responseBody, extractFileName, zipFileResponse);
          
          Map<String, Object> map = new HashMap<>();
          map.put("projectStructure", fileService.getNode(new File(extractFileName)));
          map.put("downloadUrl", zipFileResponse);
        ResponseDto responseDto = ResponseDto.builder()
                .path(httpServletRequest.getServletPath())
                .status(HttpStatus.OK.value())
                .message("fetch boilerplate successfully!")
                .data(map)
                .build();
          return ResponseEntity.ok(responseDto);
      } else {
        throw new ConflictException("Fail to download");
      }
    }catch (RuntimeException | IOException e){
      throw new RuntimeException(e.getMessage());
    }
  }
  private String getString(CreateBoilerplateDto dto, StringBuilder dependencies) {
    MetadataDto metadataDto = dto.getMetadata();
    String apiUrl = "https://start.spring.io/starter.zip";
//    String apiUrl = "https://start.spring.io/#!";
/*
    String queryParams = String.format("bootVersion=%s&type=%s&packaging=%s&jvmVersion=%d"
                    + "&groupId=%s&artifactId=%s&name=%s&description=%s" + "&dependencies=%s",
            dto.getBootVersion(), dto.getType(), metadataDto.getPackaging(),
            metadataDto.getJvmVersion(), metadataDto.getGroupId(), metadataDto.getArtifactId(),
            metadataDto.getName(), metadataDto.getDescription(), dependencies);
*/
    String packageName = metadataDto.getGroupId()+"."+metadataDto.getArtifactId();
    String queryParams = String.format("packageName=%s&language=java&bootVersion=%s&type=%s&packaging=%s&javaVersion=%d"
                    + "&groupId=%s&artifactId=%s&name=%s&description=%s" + "&dependencies=%s",
            packageName,dto.getBootVersion(), dto.getType(), metadataDto.getPackaging(),
            metadataDto.getJvmVersion(), metadataDto.getGroupId(), metadataDto.getArtifactId(),
            metadataDto.getName(), metadataDto.getDescription(), dependencies);
    
    String urlString = apiUrl + "?" + queryParams;
    return urlString;
  }
  
  private byte[] downloadTemplateFromStartSpringIo(CreateBoilerplateDto dto) {
    StringBuilder dependencies = new StringBuilder();
    List<DependencyDto> dependencyDtos = dto.getDependencies();
    for (DependencyDto dependencyDto : dependencyDtos) {
      if(dependencyDto.getId().equals("general")){
        continue;
      }
      dependencies.append(",").append(dependencyDto.getId());
    }
    /*remove first char ','*/
    dependencies.delete(0, 1);
    
    String urlString = getString(dto, dependencies);

    WebClient webClient = WebClient.create();
    try {
      return webClient.get()
              .uri(urlString)
              .retrieve()
              .bodyToMono(byte[].class)
              .block();
    } catch (RuntimeException e) {
      System.out.println("Download template error: " + e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }
  

  
  private ZipFile getFile(CreateBoilerplateDto dto, String zipFileName, byte[] responseBody,
                          String extractFileName, String zipFileResponse) throws IOException {
    try {

      /* Write .zip file from start.spring.io to folder */

      Files.write(Path.of(zipFileName), responseBody);

      /* Extract .zip file */
      ZipFile zipFile = new ZipFile(zipFileName);
      zipFile.extractAll(extractFileName);


      /*
       * Validate value
       * */

      /* Write config to application.properties */
      File file = new File(extractFileName + "/src/main/resources/application.properties");
      ApplicationFileTemplate.writeArrayListToFile(dto.getDependencies(),
          file.toPath().toString());

      /*Package*/
      String packagePath =
          extractFileName + "/src/main/java/" + dto.getMetadata().getGroupId().replace(".", "/")
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

      String serviceFolderPath = "";
      String controllerFolderPath = "";
      if (dto.getCrud()) {
        /*Service*/
        serviceFolderPath = packagePath + "/service";
        File serviceFolder = new File(serviceFolderPath);
        serviceFolder.mkdir();

        /*Controller*/
        controllerFolderPath = packagePath + "/controller";
        File controllerFolder = new File(controllerFolderPath);
        controllerFolder.mkdir();
      }

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

        if (dto.getCrud()) {
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
          ControllerTemplate.createControllerFile(packageName, entityDto, controllerFilePath,
              controllerPackagePath);
        }
      }
      /*Exception*/
      File exceptionSource = new File("shared/exception");
      File exceptionDir = new File(packagePath + "/exception");
      FileUtils.copyDirectory(exceptionSource, exceptionDir);

      String exceptionPackagePath = "package " + packageName + ".exception;";
      String responseDtoPath = packageName + ".dto" + ".ResponseDto";
      ExceptionTemplate.writePackageToExceptionFile(exceptionDir.getPath(), exceptionPackagePath,
          responseDtoPath);

      /*Zip folder to file*/
      ZipFile zipFolderToFile = new ZipFile(zipFileResponse);
      zipFolderToFile.addFolder(new File(extractFileName));
      return zipFolderToFile;
    } catch (RuntimeException exception) {
      System.out.println("get File Error:" + exception.getMessage());
      throw new RuntimeException(exception.getMessage());
    }

  }
}
