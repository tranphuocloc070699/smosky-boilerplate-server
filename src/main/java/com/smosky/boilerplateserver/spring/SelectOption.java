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
public class SelectOption {
  @Id
  @GeneratedValue
  private Integer id;

  @Column
  private String label;

  @Column
  private String value;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="property_id")
  private Property property;
}
