package com.smosky.boilerplateserver.spring;

import com.smosky.boilerplateserver.spring.dtos.ReviewDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {


  @Query(
      "SELECT DISTINCT new com.smosky.boilerplateserver.spring.dtos.ReviewDto(r.id,r.name,r.email,r.content,r.star) "
          + "FROM Review r "
          + "INNER JOIN r.boilerplate "
          + "WHERE r.boilerplate.id=:id")
  List<ReviewDto> findAllByBoilerplateId(Integer id);
}
