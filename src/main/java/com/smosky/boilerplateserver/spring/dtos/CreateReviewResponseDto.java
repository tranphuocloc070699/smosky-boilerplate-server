package com.smosky.boilerplateserver.spring.dtos;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewResponseDto {
  private Integer id;
  private String email;
  private String name;
  private String content;
  private Object star;
  private Object createdAt;
  private Object updatedAt;
  private Object totalReview;
  private Object starAvg;
  private Object oneStar;
  private Object twoStar;
  private Object threeStar;
  private Object fourStar;
  private Object fiveStar;
}
