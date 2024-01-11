package com.smosky.boilerplateserver.spring;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpringDependency {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @Column
  private String name;
  @Column
  private String description;

  @OneToMany(mappedBy = "dependency")
  private ArrayList<SpringProperty> properties;
}