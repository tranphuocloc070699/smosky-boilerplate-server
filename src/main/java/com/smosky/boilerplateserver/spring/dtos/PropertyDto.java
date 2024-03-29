package com.smosky.boilerplateserver.spring.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PropertyDto {
  private String id;
  private String value;
}
