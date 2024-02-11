package com.smosky.boilerplateserver.blog.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.List;
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
    name = "posts"
)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Post {
  @Id
  @SequenceGenerator(
      name = "posts_id_seq",
      sequenceName = "posts_id_seq",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "tbl_story_id_seq"
  )
  private Integer id;

  @Column(nullable = false)
  private String thumbnail;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String slug;

  @Column(nullable = false,columnDefinition = "TEXT")
  private String content;

  @Column(nullable = false,columnDefinition = "TEXT")
  private String preContent;

  @OneToMany(mappedBy = "post")
  private List<Toc> tocs;

  @Column(name="created_at")
  private Date createdAt;

  @Column(name="updated_at")
  private Date updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = new Date();
    updatedAt = new Date();
  }

  @PreUpdate
  protected void onUpdate(){
    updatedAt = new Date();
  }

}

