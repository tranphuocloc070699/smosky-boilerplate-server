package com.loctran.abc.repository;

import com.loctran.abc.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Integer> {}
