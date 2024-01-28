package com.smosky.boilerplateserver.spring.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreateEntityDependencyId {

  String name;
  String id;
}
