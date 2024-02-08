package com.smosky.boilerplateserver.spring;

import com.smosky.boilerplateserver.spring.dtos.BoilerplateDetailDto;
import com.smosky.boilerplateserver.spring.dtos.BoilerplateWithReviewCountingDto;
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
      /*"select  b.id,b.name,b.description,COUNT(r) AS amount,AVG(r.star) AS star_avg from boilerplate b \n"
          + "inner join review r on b.id=r.boilerplate_id\n"
          + "group by b.id,b.name,b.description;\n", nativeQuery = true)*/
      "SELECT new com.smosky.boilerplateserver.spring.dtos.BoilerplateWithReviewCountingDto(b.id,b.name,b.description,b.thumbnail,COUNT(r),COALESCE(AVG(r.star),0) )"
          + "FROM Boilerplate b " + "LEFT JOIN b.reviews r "
          + "GROUP BY b.id, b.name, b.description, b.thumbnail")
  List<Object> findAllWithStarCounting();
}
