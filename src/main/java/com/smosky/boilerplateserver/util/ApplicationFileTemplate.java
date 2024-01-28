package com.smosky.boilerplateserver.util;

import com.smosky.boilerplateserver.spring.dtos.DependencyDto;
import com.smosky.boilerplateserver.spring.dtos.PropertyDto;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ApplicationFileTemplate {

  public static void writeArrayListToFile(List<DependencyDto> arrayList, String filePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      CustomWriter customWriter = new CustomWriter(writer);
      for (DependencyDto line : arrayList) {
        customWriter.write("# ", line.getId());
        for (PropertyDto propertyDto : line.getProperties()) {
          customWriter.write(propertyDto.getId(), "=", propertyDto.getDefaultValue());
        }
      }
    } catch (IOException e) {
      e.printStackTrace(); // Handle the exception based on your requirements
    }
  }
}
