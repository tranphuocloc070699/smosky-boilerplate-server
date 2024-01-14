package com.smosky.boilerplateserver.spring;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
public class DependencyType {

  @Id
  @GeneratedValue
  private Integer id;

  @Column
  private String name;

  @Column
  @Enumerated
  private LanguageType languageType;


  @OneToMany(mappedBy = "type")
  private List<Dependency> dependencies;


}
