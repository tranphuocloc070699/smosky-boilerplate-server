package com.smosky.boilerplateserver.spring;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="tbl_dependency")
public class Dependency {
  @Id
  private String id;

  @Column
  private String name;

  @Column
  private String description;

  @OneToMany(mappedBy = "dependency")
  private List<Property> properties;
}