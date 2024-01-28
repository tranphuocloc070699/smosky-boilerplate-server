package com.smosky.boilerplateserver.util;

import com.smosky.boilerplateserver.spring.dtos.EntityDto;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RepositoryTemplate {

  public static void createRepositoryFile(String packageName, EntityDto entityDto,
      String repositoryFilePath, String repositoryPackagePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(repositoryFilePath))) {
      CustomWriter customWriter = new CustomWriter(writer);
      customWriter.write(2, repositoryPackagePath + ";");
      customWriter.write("import ", packageName, ".entity.", entityDto.getName(), ";");
      customWriter.write(2, "import org.springframework.data.jpa.repository.JpaRepository;");
      customWriter.write("public interface ", entityDto.getName(),
          "Repository extends JpaRepository<",
          entityDto.getName(), ",Integer> {}");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
