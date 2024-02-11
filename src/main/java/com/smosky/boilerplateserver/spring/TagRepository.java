package com.smosky.boilerplateserver.spring;

import com.smosky.boilerplateserver.spring.dtos.ReviewDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

  public Optional<Tag> findByName(String name);
}
