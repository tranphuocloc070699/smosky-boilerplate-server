package com.smosky.boilerplateserver.spring.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EntityDto {
  private String name;
  private List<TemplateDto> templates;
}
