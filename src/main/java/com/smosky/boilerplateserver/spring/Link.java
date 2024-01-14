package com.smosky.boilerplateserver.spring;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
public class Link {
  @Id
  @GeneratedValue
  private Integer id;

  @Column
  private String title;

  @Column
  private String name;

  @Column
  private String url;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="dependency_id")
  private Dependency dependency;
}
