package com.loctran.abc.repository;

import com.loctran.abc.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Integer> {}
