package com.smosky.boilerplateserver.spring.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoilerplateWithReviewCountingDto {

  private Integer id;
  private String name;
  private String description;
  private String thumbnail;
  private Long totalReview;
  private Double starAvg;

}

