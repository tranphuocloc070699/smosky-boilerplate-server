package com.smosky.boilerplateserver.util;

import com.smosky.boilerplateserver.shared.Constant;
import com.smosky.boilerplateserver.spring.dtos.CreateEntityDependencyId;
import com.smosky.boilerplateserver.spring.dtos.EntityDto;
import com.smosky.boilerplateserver.spring.dtos.TemplateDto;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServiceTemplate {

  private static List<String> defaultTypes = Constant.DEFAULT_JAVA_TYPE;

  public static void createServiceFile(String packageName, EntityDto entityDto,
      String serviceFilePath,
      String servicePackagePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(serviceFilePath))) {
      CustomWriter customWriter = new CustomWriter(writer);
      customWriter.write(2, servicePackagePath + ";");

      customWriter.write("import ", packageName, ".entity.", entityDto.getName(), ";");
      customWriter.write("import ", packageName, ".dao.", entityDto.getName(), "DataAccess", ";");
      customWriter.write("import ", packageName, ".mapper.", entityDto.getName(), "Mapper", ";");
      customWriter.write(2, "import ", packageName, ".dto.", entityDto.getName(), "Dto", ";");

      for (TemplateDto templateDto : entityDto.getTemplates()) {
        if (!defaultTypes.contains(templateDto.getType())) {
          customWriter.write("import ", packageName, ".entity.", templateDto.getName(), ";");
          customWriter.write(2, "import ", packageName, ".dao.", templateDto.getName(),
              "DataAccess", ";");
        }
      }
      customWriter.write("import ", packageName, ".exception", ".ConflictException;");
      customWriter.write("import ", packageName, ".exception", ".ResourceNotFoundException;");
      customWriter.write("import lombok.RequiredArgsConstructor;");
      customWriter.write("import org.springframework.data.domain.Page;");
      customWriter.write("import org.springframework.data.domain.PageRequest;");
      customWriter.write("import org.springframework.data.domain.Pageable;");
      customWriter.write("import org.springframework.data.domain.Sort;");
      customWriter.write(2, "import org.springframework.stereotype.Service;");

      customWriter.write("@Service");
      customWriter.write("@RequiredArgsConstructor");
      customWriter.write("public class ", entityDto.getName(), "Service {");
      customWriter.write("\t private final ", entityDto.getName(), "DataAccess dataAccess;");
      for (TemplateDto templateDto : entityDto.getTemplates()) {
        if (!defaultTypes.contains(templateDto.getType())) {
          customWriter.write("\t private final ", templateDto.getName(), "DataAccess ",
              templateDto.getName().toLowerCase(), "DataAccess;");
        }
      }

      writer.newLine();
      String createEntityDependencyIds = "";
      List<CreateEntityDependencyId> entityDependencyIds = new ArrayList<>();
      for (TemplateDto templateDto : entityDto.getTemplates()) {
        if ((templateDto.getType().equals("OneToOne") && !templateDto.getMappedBy().isEmpty()) || (
            templateDto.getType().equals("ManyToOne") && !templateDto.getReferencedColumnName()
                .isEmpty())) {
          createEntityDependencyIds += "Integer " + templateDto.getName().toLowerCase() + "Id,";
          entityDependencyIds.add(
              CreateEntityDependencyId.builder().id(templateDto.getName().toLowerCase() + "Id")
                  .name(templateDto.getName()).build());
        }

      }
      /*Start createEntity Method*/
      customWriter.write("\tpublic ", entityDto.getName(), " createEntity(",
          createEntityDependencyIds.toString(), "",
          entityDto.getName(), "Dto dto) {");

      customWriter.write("\t \t ", entityDto.getName(), " entity = ", entityDto.getName(),
          ".builder().build();");
      customWriter.write("\t \t ", entityDto.getName(), "Mapper.mapToEntity(entity, dto);");
      for (CreateEntityDependencyId id : entityDependencyIds) {
        customWriter.write("\t \t", id.getName(), " ", id.getName().toLowerCase(), " = ",
            id.getName().toLowerCase(), "DataAccess.findById(", id.getId(),
            ").orElseThrow(() -> new ResourceNotFoundException(\"", id.getName(), "\",\"",
            id.getId(), "\",", id.getId(), ".toString()));");
        customWriter.write("\t \t", "entity.set", StringUtils.capitalizeFirstLetter(id.getName()),
            "(", id.getName().toLowerCase(),
            ");");
      }
      customWriter.write("\t \t", "return dataAccess.save(entity);");
      /*End createEntity Method*/
      customWriter.write(2, "\t}");

      /*Start fetchEntity Method*/
      customWriter.write("\t public ", entityDto.getName(), " fetchEntity(Integer id) {");
      customWriter.write(
          "\t \t return dataAccess.findById(id).orElseThrow(() -> new ResourceNotFoundException(\"",
          entityDto.getName(), "\",\"Id\",id.toString()));");

      /*End fetchEntity Method*/
      customWriter.write("\t}");

      /*Start fetchEntities Method*/
      customWriter.write("\t public Page<", entityDto.getName(),
          "> fetchEntities(int currentPage,int pageSize ,String sortBy, String sortDir) {");
      customWriter.write("\t \t Sort sort = Sort.by(sortBy);");
      customWriter.write(
          "\t \t sort = sortDir.equals(\"asc\") ? sort.ascending() : sort.descending();");
      customWriter.write(2,
          "\t \t Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);");
      customWriter.write("\t \t return dataAccess.findAll(pageable);");
      /*End fetchEntities Method*/
      customWriter.write("\t}");

      /*Start updateEntity Method*/
      customWriter.write("\t  public ", entityDto.getName(), " updateEntity(", entityDto.getName(),
          "Dto dto,Integer id) {");
      customWriter.write("\t \t boolean isUpdated = false;");
      customWriter.write(2, "\t \t ", entityDto.getName(),
          " modelExisted = dataAccess.findById(id).orElseThrow(() -> new ResourceNotFoundException(\"",
          entityDto.getName(), "\",\"Id\",id.toString()));");
      for (TemplateDto templateDto : entityDto.getTemplates()) {
        if (defaultTypes.contains(templateDto.getType())) {
          customWriter.write("\t \t \tif (!modelExisted.get",
              StringUtils.capitalizeFirstLetter(templateDto.getName()),
              "().equals(dto.get", StringUtils.capitalizeFirstLetter(templateDto.getName()),
              "())) {");
          customWriter.write("\t \t \tmodelExisted.set",
              StringUtils.capitalizeFirstLetter(templateDto.getName()), "(dto.get",
              StringUtils.capitalizeFirstLetter(templateDto.getName()), "());");
          customWriter.write("\t \t \tisUpdated=true;");
          customWriter.write("\t \t}");
        }
      }

      customWriter.write(
          "\t \t if(!isUpdated) throw new ConflictException(\"Property not changing\");");
      customWriter.write("\t \t dataAccess.save(modelExisted);");
      customWriter.write("\t \t return modelExisted;");
      /*End updateEntity Method*/
      customWriter.write(2, "\t}");

      /*Start deleteEntity Method*/
      customWriter.write("\t  public ", entityDto.getName(), " deleteEntity(Integer id) {");
      customWriter.write("\t \t ", entityDto.getName(),
          " modelExisted = dataAccess.findById(id).orElseThrow(() -> new ResourceNotFoundException(\"",
          entityDto.getName(), "\",\"Id\",id.toString()));");
      customWriter.write("\t \tdataAccess.delete(id);");
      customWriter.write("\t \treturn modelExisted;");

      /*End delete Entity method*/
      customWriter.write(2, "\t};");

      for (TemplateDto templateDto : entityDto.getTemplates()) {
        if (templateDto.getType().equals("ManyToMany") && !templateDto.getReferencedColumnName()
            .isEmpty()) {
          /*Start approve method*/
          customWriter.write("\t public ", entityDto.getName(), " add", templateDto.getName(),
              "(Integer id,Integer ", templateDto.getName().toLowerCase(), "Id) {");
          customWriter.write("\t \t ", entityDto.getName(),
              " modelExisted = dataAccess.findById(id).orElseThrow(() -> new ResourceNotFoundException(\"",
              entityDto.getName(), "\",\"Id\",id.toString()));");
          customWriter.write("\t \t  ", templateDto.getName(), " ",
              templateDto.getName().toLowerCase(), "Existed = userDataAccess.findById(",
              templateDto.getName().toLowerCase(),
              "Id).orElseThrow(() -> new ResourceNotFoundException(\"", templateDto.getName(),
              "\",\"Id\",", templateDto.getName().toLowerCase(), "Id.toString()));");
          customWriter.write("\t \tmodelExisted.get",
              StringUtils.capitalizeFirstLetter(templateDto.getName()), "s().add(",
              templateDto.getName().toLowerCase(), "Existed);");
          customWriter.write("\t \t modelExisted.set",
              StringUtils.capitalizeFirstLetter(templateDto.getName()), "s(modelExisted.get",
              StringUtils.capitalizeFirstLetter(templateDto.getName()), "s());");
          customWriter.write("\t \t dataAccess.save(modelExisted);");
          customWriter.write("\t \t return modelExisted;");

          /*End approve method*/
          customWriter.write(2, "\t }");

          /*Start removeEntity*/
          customWriter.write("\t public ", entityDto.getName(), " remove", templateDto.getName(),
              "(Integer id,Integer ", templateDto.getName().toLowerCase(), "Id) {");
          customWriter.write("\t \t ", entityDto.getName(),
              " modelExisted = dataAccess.findById(id).orElseThrow(() -> new ResourceNotFoundException(\"",
              entityDto.getName(), "\",\"Id\",id.toString()));");
          customWriter.write("\t \t  ", templateDto.getName(), " ",
              templateDto.getName().toLowerCase(), "Existed = userDataAccess.findById(",
              templateDto.getName().toLowerCase(),
              "Id).orElseThrow(() -> new ResourceNotFoundException(\"", templateDto.getName(),
              "\",\"Id\",", templateDto.getName().toLowerCase(), "Id.toString()));");
          customWriter.write("\t \t modelExisted.get",
              StringUtils.capitalizeFirstLetter(templateDto.getName()), "s().remove(",
              templateDto.getName().toLowerCase(), "Existed);");
          customWriter.write("\t \t  modelExisted.set",
              StringUtils.capitalizeFirstLetter(templateDto.getName()), "s(modelExisted.get",
              templateDto.getName(), "s());");
          customWriter.write("\t \t dataAccess.save(modelExisted);");
          customWriter.write("\t \t return modelExisted;");
          customWriter.write("\t}");
        }
      }


      /*End class*/
      customWriter.write("}");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
