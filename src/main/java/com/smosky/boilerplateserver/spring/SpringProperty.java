package com.smosky.boilerplateserver.spring;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpringProperty {
  @Id
  private Integer id;

  @Column
  private String name;

  @Column
  private String value;

  @ManyToOne
  @JoinColumn(name = "dependency_id")
  private SpringDependency dependency;
}
