package com.smosky.boilerplateserver.blog.dtos;

import com.smosky.boilerplateserver.blog.entity.Toc;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UpSavePostDto {
  private String thumbnail;
  private String title;
  private String slug;
  private String content;
  private String preContent;
  private List<TocDto> toc;
}
