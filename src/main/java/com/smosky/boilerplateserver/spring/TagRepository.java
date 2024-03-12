package com.smosky.boilerplateserver.spring;

import java.util.Optional;

import com.smosky.boilerplateserver.spring.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

  public Optional<Tag> findByName(String name);
}
