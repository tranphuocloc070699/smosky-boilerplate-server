package com.smosky.boilerplateserver.spring;

import com.smosky.boilerplateserver.spring.dtos.ReviewDto;

import java.util.List;

import com.smosky.boilerplateserver.spring.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {


  @Query(
      "SELECT DISTINCT new com.smosky.boilerplateserver.spring.dtos.ReviewDto(r.id,r.name,r.email,r.content,r.star,r.createdAt,r.updatedAt) "
          + "FROM Review r "
          + "INNER JOIN r.boilerplate "
          + "WHERE r.boilerplate.id=:id")
  List<ReviewDto> findAllByBoilerplateId(Integer id);

  @Query(
      "SELECT DISTINCT new com.smosky.boilerplateserver.spring.dtos.CreateReviewResponseDto(r.id,r.email,r.name,r.content,r.star,r.createdAt,r.updatedAt,"
          + "COUNT(r),"
          + "COALESCE(AVG(r.star),0),"
          + "COUNT(DISTINCT CASE WHEN r.star = 1 THEN 1 ELSE null END),"
          + "COUNT(DISTINCT CASE WHEN r.star = 2 THEN 1 ELSE null END),"
          + "COUNT(DISTINCT CASE WHEN r.star = 3 THEN 1 ELSE null END),"
          + "COUNT(DISTINCT CASE WHEN r.star = 4 THEN 1 ELSE null END),"
          + "COUNT(DISTINCT CASE WHEN r.star = 5 THEN 1 ELSE null END)) "
          + "FROM Boilerplate b "
          + "LEFT JOIN b.reviews r "
          + "WHERE b.id=:id "
          + "GROUP BY r.id"
  )
  Object findReview(@Param("id") Integer id);



}
