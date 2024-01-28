package com.smosky.boilerplateserver.util;

import com.smosky.boilerplateserver.spring.dtos.EntityDto;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DaoTemplate {

  public static void createDaoFile(String packageName, EntityDto entityDto, String daoFilePath,
      String daoPackagePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(daoFilePath))) {
      CustomWriter customWriter = new CustomWriter(writer);
      customWriter.write(2, daoPackagePath + ";");
      customWriter.write("import ", packageName, ".entity.", entityDto.getName(), ";");
      customWriter.write(2, "import ", packageName, ".repository.", entityDto.getName(),
          "Repository", ";");
      customWriter.write("import lombok.RequiredArgsConstructor;");
      customWriter.write("import org.springframework.data.domain.Page;");
      customWriter.write("import org.springframework.data.domain.Pageable;");
      customWriter.write("import org.springframework.stereotype.Repository;");
      customWriter.write(2, "import java.util.Optional;");
      customWriter.write("@Repository");
      customWriter.write("@RequiredArgsConstructor");
      customWriter.write(2, "public class ", entityDto.getName(), "DataAccess {");
      customWriter.write(2, "private final ", entityDto.getName(), "Repository repository;");
      /*find by Id*/
      customWriter.write("public Optional<", entityDto.getName(), "> findById(Integer id) {");
      customWriter.write("\t return repository.findById(id);");
      customWriter.write("}");
      /*save*/
      customWriter.write("public ", entityDto.getName(), " save(", entityDto.getName(),
          " entity) {");
      customWriter.write("\t return repository.save(entity);");
      customWriter.write("}");
      /*find All*/
      customWriter.write("public Page<", entityDto.getName(), "> findAll(Pageable pageable) {");
      customWriter.write("\t return repository.findAll(pageable);");
      customWriter.write("}");
      /*delete*/
      customWriter.write("public void delete(Integer id) {");
      customWriter.write("\t repository.deleteById(id);");
      customWriter.write("}");
      /*End class*/
      customWriter.write("}");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
