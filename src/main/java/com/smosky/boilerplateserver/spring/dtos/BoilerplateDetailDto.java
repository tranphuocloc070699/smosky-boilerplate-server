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
  String thumbnail;
  String previewLink;
  String description;
  Long totalReview;
  Double starAvg;
  Long oneStar;
  Long twoStar;
  Long threeStar;
  Long fourStar;
  Long fiveStar;
  List<ReviewDto> reviews;
  Object dependenciesSelected;
  Object projectStructure;
  Object dependencies;
  Object features;


  public BoilerplateDetailDto(Integer id, String name, String thumbnail,String previewLink,String description, Long totalReview,
      Double starAvg, Long oneStar, Long twoStar, Long threeStar, Long fourStar, Long fiveStar,Object dependenciesSelected,Object features) {
    this.id = id;
    this.name = name;
    this.thumbnail = thumbnail;
    this.previewLink = previewLink;
    this.description = description;
    this.totalReview = totalReview;
    this.starAvg = starAvg;
    this.oneStar = oneStar;
    this.twoStar = twoStar;
    this.threeStar = threeStar;
    this.fourStar = fourStar;
    this.fiveStar = fiveStar;
    this.dependenciesSelected = dependenciesSelected;
    this.features = features;
  }
}
