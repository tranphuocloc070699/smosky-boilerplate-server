package com.smosky.boilerplateserver.blog.dtos;

import com.smosky.boilerplateserver.blog.entity.Toc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TocDto {
  private String title;
  private String link;
  private String type;
}
