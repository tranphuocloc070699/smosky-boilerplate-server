package com.smosky.boilerplateserver.spring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SelectOptionRepository extends JpaRepository<SelectOption,Integer> {

}
