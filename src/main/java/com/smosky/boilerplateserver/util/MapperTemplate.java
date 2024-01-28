package com.smosky.boilerplateserver.util;

import com.smosky.boilerplateserver.shared.Constant;
import com.smosky.boilerplateserver.spring.dtos.EntityDto;
import com.smosky.boilerplateserver.spring.dtos.TemplateDto;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MapperTemplate {

  private static List<String> defaultTypes = Constant.DEFAULT_JAVA_TYPE;

  public static void createMapperFile(String packageName, EntityDto entityDto,
      String mapperPackagePath,
      String mapperFilePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(mapperFilePath))) {
      CustomWriter customWriter = new CustomWriter(writer);
      customWriter.write(2, mapperPackagePath + ";");
      customWriter.write("import ", packageName, ".dto.", entityDto.getName(), "Dto", ";");
      customWriter.write("import ", packageName, ".entity.", entityDto.getName(), ";");

      customWriter.write(2, "public class ", entityDto.getName(), "Mapper", " {");
      customWriter.write("public static", " ", entityDto.getName(), " ", "mapToEntity(",
          entityDto.getName(), " ", "entity,", entityDto.getName(), "Dto dto) {");
      for (TemplateDto templateDto : entityDto.getTemplates()) {
        if (defaultTypes.contains(templateDto.getType())) {
          customWriter.write("entity.set", StringUtils.capitalizeFirstLetter(templateDto.getName()),
              "(dto.get", StringUtils.capitalizeFirstLetter(templateDto.getName()),
              "());");
        }
      }
      customWriter.write("return entity;");
      /*End mapToEntity method*/
      customWriter.write("}");
      /*End class*/
      customWriter.write("}");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
