package com.smosky.boilerplateserver.util;

import com.smosky.boilerplateserver.shared.Constant;
import com.smosky.boilerplateserver.spring.dtos.EntityDto;
import com.smosky.boilerplateserver.spring.dtos.TemplateDto;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DtoTemplate {

  public static void createDto(EntityDto dto, String dtoPackagePath, String dtoFilePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(dtoFilePath))) {
      List<String> defaultTypes = Constant.DEFAULT_JAVA_TYPE;
      CustomWriter customWriter = new CustomWriter(writer);
      customWriter.write(2, dtoPackagePath, ";");
      customWriter.write("import lombok.AllArgsConstructor;");
      customWriter.write("import lombok.Builder;");
      customWriter.write("import lombok.Data;");
      customWriter.write(2, "import lombok.NoArgsConstructor;");

      customWriter.write("@Data");
      customWriter.write("@AllArgsConstructor");
      customWriter.write("@Builder");
      customWriter.write("public class " + dto.getName() + "Dto {");
      for (TemplateDto templateDto : dto.getTemplates()) {
        if (defaultTypes.contains(templateDto.getType())) {
          customWriter.write(2, "private ", templateDto.getType(), " ", templateDto.getName(), ";");
        } else {
      /*    switch (templateDto.getType()) {
            case "OneToOne":
            case "ManyToOne":
              customWriter.write(2, "private ", templateDto.getName(), " ",
                  templateDto.getName().toLowerCase(), ";");
              break;
            case "OneToMany":
              customWriter.write(2, "private ", "List<", templateDto.getName(), ">", " ",
                  templateDto.getName().toLowerCase(), "s;");
              break;
            case "ManyToMany":
              customWriter.write(2, "private List<", templateDto.getName(), ">", " ",
                  templateDto.getName().toLowerCase(), "s;");
              break;
            default:
              break;
          }*/
        }
      }
      customWriter.write("}");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void createResponseDto(String dtoPackagePath, String responseDtoFilePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(responseDtoFilePath))) {

      CustomWriter customWriter = new CustomWriter(writer);
      customWriter.write(2, dtoPackagePath + ";");
      customWriter.write("import lombok.AllArgsConstructor;");
      customWriter.write("import lombok.Builder;");
      customWriter.write("import lombok.Data;");
      customWriter.write("import org.springframework.http.HttpStatus;");
      customWriter.write(2, "import java.util.Date;");

      customWriter.write("@Data");
      customWriter.write("@AllArgsConstructor");
      customWriter.write("@Builder");
      customWriter.write("public class ResponseDto {");
      customWriter.write("private HttpStatus status;");
      customWriter.write("private Object data;");
      customWriter.write("private Object errors;");
      customWriter.write("private String message;");
      customWriter.write("private Date timestamp;");
      customWriter.write("private String path;");
      customWriter.write("}");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
