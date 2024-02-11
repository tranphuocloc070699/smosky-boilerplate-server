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
public class CreateBoilerplateDto {
    private String type;
    private String bootVersion;
    private MetadataDto metadata;
    private List<DependencyDto> dependencies;
    private List<EntityDto> entities;
    private Boolean crud;
}

