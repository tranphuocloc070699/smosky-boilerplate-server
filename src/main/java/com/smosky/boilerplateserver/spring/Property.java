package com.smosky.boilerplateserver.spring;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
public class Property {
  @Id
  private String id;

  @Column
  private String title;

  @Column
  private String value;

  @Column
  private String toolTip;

  @Column
  private Boolean isDisable;


  @OneToMany(mappedBy = "property")
  private List<SelectOption> options;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="dependency_id")
  private Dependency dependency;

}