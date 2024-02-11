package com.smosky.boilerplateserver.spring;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
public class Dependency {
  @Id
  private String id;

  @Column
  private String name;

  @Column
  private String description;

  @Column
  private String notice;


  @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
  @CollectionTable(name = "compatibilityRanges", joinColumns = @JoinColumn(name = "dependency_id"))
  @Column
  private List<String> compatibilityRanges;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="type_id")
  private DependencyType type;


    @OneToMany(mappedBy = "dependency")
  private List<Property> properties;

  @OneToMany(mappedBy = "dependency")
  private List<Link> links;


}
