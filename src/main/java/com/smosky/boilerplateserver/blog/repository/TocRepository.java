package com.smosky.boilerplateserver.blog.repository;

import com.smosky.boilerplateserver.blog.entity.Toc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TocRepository extends JpaRepository<Toc,Integer> {

}
