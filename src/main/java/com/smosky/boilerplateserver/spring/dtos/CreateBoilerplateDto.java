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


}

//    String queryParams = "bootVersion=3.2.1&type=maven-project&packaging=jar&jvmVersion=17" +
//        "&groupId=com.loctran&artifactId=demo&name=demo&description=description" +
//        "&dependencies=lombok,web,data-jpa,postgresql";
