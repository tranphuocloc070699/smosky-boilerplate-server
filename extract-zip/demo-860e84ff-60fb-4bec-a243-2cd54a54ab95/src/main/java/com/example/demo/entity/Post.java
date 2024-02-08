package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_post")
public class Post{
@Id
@GeneratedValue
private Integer id;

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name="user_id")
private User user;

}
