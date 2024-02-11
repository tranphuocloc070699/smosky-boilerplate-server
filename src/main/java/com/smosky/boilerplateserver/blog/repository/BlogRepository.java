package com.smosky.boilerplateserver.blog.repository;

import com.smosky.boilerplateserver.blog.entity.Post;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Post,Integer> {

  Optional<Post> findBySlug(String slug);
}
