package com.smosky.boilerplateserver.spring.dtos;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

  Integer id;
  String name;
  String email;
  String content;
  Integer star;
  Date createdAt;
  Date updatedAt;
}
