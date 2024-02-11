package com.smosky.boilerplateserver.util;

import com.smosky.boilerplateserver.spring.dtos.CreateEntityDependencyId;
import com.smosky.boilerplateserver.spring.dtos.EntityDto;
import com.smosky.boilerplateserver.spring.dtos.TemplateDto;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerTemplate {

  public static void createControllerFile(String packageName, EntityDto entityDto, String controllerFilePath, String controllerPackagePath) {
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
}
