package com.smosky.boilerplateserver.spring.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
public class Tag {
  @Id
  @GeneratedValue
  private Integer id;

  @Column(unique = true)
  private String name;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @JoinTable(name = "tag_boilerplate",
      joinColumns = @JoinColumn(name = "tag_id"),
      inverseJoinColumns = @JoinColumn(name = "boilerplate_id")
  )
  private List<Boilerplate> boilerplates;
}
