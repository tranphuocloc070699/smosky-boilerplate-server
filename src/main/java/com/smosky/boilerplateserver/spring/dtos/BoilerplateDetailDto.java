package com.smosky.boilerplateserver.spring.dtos;

import com.smosky.boilerplateserver.spring.Review;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoilerplateDetailDto {

  Integer id;
  String name;
  String description;
  Long totalReview;
  Double starAvg;
  Long oneStar;
  Long twoStar;
  Long threeStar;
  Long fourStar;
  Long fiveStar;
  List<ReviewDto> reviews;

  public BoilerplateDetailDto(Integer id, String name, String description, Long totalReview,
      Double starAvg, Long oneStar, Long twoStar, Long threeStar, Long fourStar, Long fiveStar) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.totalReview = totalReview;
    this.starAvg = starAvg;
    this.oneStar = oneStar;
    this.twoStar = twoStar;
    this.threeStar = threeStar;
    this.fourStar = fourStar;
    this.fiveStar = fiveStar;
  }
}
