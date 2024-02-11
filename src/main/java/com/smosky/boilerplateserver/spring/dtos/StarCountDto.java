package com.smosky.boilerplateserver.spring.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StarCountDto {
  private Long totalReviews;
  private Double starAvg;
  private Long fiveStarCount;
  private Long fourStarCount;
  private Long threeStarCount;
  private Long twoStarCount;
  private Long oneStarCount;

}