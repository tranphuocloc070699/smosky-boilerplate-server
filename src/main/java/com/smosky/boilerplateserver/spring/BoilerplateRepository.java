package com.smosky.boilerplateserver.spring;

import com.smosky.boilerplateserver.spring.dtos.BoilerplateDetailDto;
import com.smosky.boilerplateserver.spring.dtos.BoilerplateWithReviewCountingDto;
import com.smosky.boilerplateserver.spring.dtos.StarCountDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BoilerplateRepository extends JpaRepository<Boilerplate, Integer> {

  @Query(
      "SELECT new com.smosky.boilerplateserver.spring.dtos.BoilerplateDetailDto (b.id, b.name,b.thumbnail,b.previewLink ,b.description, "
          + "COUNT(DISTINCT r),COALESCE(AVG(r.star),0) , "
          + "COUNT(DISTINCT CASE WHEN r.star = 1 THEN 1 ELSE null END) AS star1Count, "
          + "COUNT(DISTINCT CASE WHEN r.star = 2 THEN 1 ELSE null END) AS star2Count, "
          + "COUNT(DISTINCT CASE WHEN r.star = 3 THEN 1 ELSE null END) AS star3Count, "
          + "COUNT(DISTINCT CASE WHEN r.star = 4 THEN 1 ELSE null END) AS star4Count, "
          + "COUNT(DISTINCT CASE WHEN r.star = 5 THEN 1 ELSE null END) AS star5Count, "
          + "STRING_AGG(dep, ', ') AS dependencies ," + "STRING_AGG(f, ', ') AS features ) "
          + "FROM Boilerplate b " + "LEFT JOIN b.reviews r " + "LEFT JOIN b.dependencies dep "
          + "LEFT JOIN b.features f " + "WHERE b.name = :name "
          + "GROUP BY b.id, b.name, b.description")
  BoilerplateDetailDto findByName(@Param("name") String name);


  @Query(value =
      "SELECT new com.smosky.boilerplateserver.spring.dtos.BoilerplateWithReviewCountingDto(b.id,b.name,b.description,b.thumbnail,COUNT(r),COALESCE(AVG(r.star),0) )"
          + "FROM Boilerplate b " + "LEFT JOIN b.reviews r "
          + "GROUP BY b.id, b.name, b.description, b.thumbnail")
  List<Object> findAllWithStarCounting();

  @Query(
      "SELECT new com.smosky.boilerplateserver.spring.dtos.CreateReviewResponseDto(r.id,r.email,r.name,r.content,r.star,r.createdAt,r.updatedAt,"
          + "COUNT(DISTINCT r),"
          + "COALESCE(AVG(r.star),0),"
          + "COUNT(DISTINCT CASE WHEN r.star = 1 THEN 1 ELSE null END) AS oneStar,"
          + "COUNT(DISTINCT CASE WHEN r.star = 2 THEN 1 ELSE null END) AS twoStar,"
          + "COUNT(DISTINCT CASE WHEN r.star = 3 THEN 1 ELSE null END) AS threeStar,"
          + "COUNT(DISTINCT CASE WHEN r.star = 4 THEN 1 ELSE null END) AS fourStar,"
          + "COUNT(DISTINCT CASE WHEN r.star = 5 THEN 1 ELSE null END) AS fiveStar) "
          + "FROM Boilerplate b "
          + "LEFT JOIN b.reviews r "
          + "WHERE b.id=:id "
          + "GROUP BY r.id"
  )
  Object[] findReview(@Param("id") Integer id);


  @Query(
      "SELECT NEW com.smosky.boilerplateserver.spring.dtos.StarCountDto(" +
          "COUNT(DISTINCT r) AS totalReviews, " +
          "COALESCE(AVG(r.star),0), " +
          "COUNT(DISTINCT CASE WHEN r.star = 5 THEN r.id ELSE null END) AS fiveStarCount, " +
          "COUNT(DISTINCT CASE WHEN r.star = 4 THEN r.id ELSE null END) AS fourStarCount, " +
          "COUNT(DISTINCT CASE WHEN r.star = 3 THEN r.id ELSE null END) AS threeStarCount, " +
          "COUNT(DISTINCT CASE WHEN r.star = 2 THEN r.id ELSE null END) AS twoStarCount, " +
          "COUNT(DISTINCT CASE WHEN r.star = 1 THEN r.id ELSE null END) AS oneStarCount) " +
          "FROM Boilerplate b " +
          "LEFT JOIN b.reviews r " +
          "WHERE b.id = :id"
  )
  StarCountDto findStarCounts(@Param("id") Integer id);
}
