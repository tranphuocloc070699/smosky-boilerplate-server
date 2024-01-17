package com.smosky.boilerplateserver.spring.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MetadataDto {
  private String groupId;
  private String artifactId;
  private String name;
  private String description;
  private String packaging;
  private Integer jvmVersion;
}
