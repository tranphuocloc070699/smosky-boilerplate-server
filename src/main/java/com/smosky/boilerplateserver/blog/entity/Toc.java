package com.smosky.boilerplateserver.blog.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "tocs"
)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Toc {
  @Id
  @GeneratedValue
  private Integer id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String link;

  @Column(nullable = false)
  private String type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="post_id")
  Post post;
}

