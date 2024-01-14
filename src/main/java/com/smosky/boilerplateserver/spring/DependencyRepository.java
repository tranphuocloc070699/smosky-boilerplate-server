package com.smosky.boilerplateserver.spring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DependencyRepository extends JpaRepository<Dependency,String> {

  @Query("SELECT d FROM Dependency d")
     List<Dependency> fetchAll();
}
