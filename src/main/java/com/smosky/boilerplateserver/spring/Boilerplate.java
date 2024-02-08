package com.smosky.boilerplateserver.spring;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
public class Boilerplate {

  @Id
  @GeneratedValue
  private Integer id;

  private String name;

  private String thumbnail;

  private String description;

  private String previewLink;

  @OneToMany(mappedBy = "boilerplate")
  private List<Review> reviews;

  @ElementCollection
  @CollectionTable(name = "boilerplate_dependencies", joinColumns = @JoinColumn(name = "boilerplate_id"))
  @Column(name = "dependencies")
  private List<String> dependencies;

  @ElementCollection
  @CollectionTable(name = "boilerplate_features", joinColumns = @JoinColumn(name = "boilerplate_id"))
  @Column(name = "features")
  private List<String> features;

  @CreationTimestamp
  @Column(name = "created_at")
  private Date createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private Date updatedAt;
}
