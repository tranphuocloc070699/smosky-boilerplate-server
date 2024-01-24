package com.smosky.boilerplateserver.spring.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TemplateDto {
  private String name;
  private String type;
  private Boolean primary;
  private String mappedBy;
  private String referencedColumnName;
}
