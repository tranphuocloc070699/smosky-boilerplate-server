package com.smosky.boilerplateserver.spring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TypeRepository extends JpaRepository<DependencyType, Integer> {
  @Query("SELECT DISTINCT t FROM DependencyType t LEFT JOIN  t.dependencies d LEFT JOIN d.compatibilityRanges LEFT JOIN d.links LEFT JOIN d.properties p LEFT JOIN p.options")
  List<DependencyType> findAllWithDependencies();

}
