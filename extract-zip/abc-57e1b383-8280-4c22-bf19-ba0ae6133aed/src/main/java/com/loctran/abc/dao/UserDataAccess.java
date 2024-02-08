package com.loctran.abc.dao;

import com.loctran.abc.entity.User;
import com.loctran.abc.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDataAccess {

private final UserRepository repository;

public Optional<User> findById(Integer id) {
	 return repository.findById(id);
}
public User save(User entity) {
	 return repository.save(entity);
}
public Page<User> findAll(Pageable pageable) {
	 return repository.findAll(pageable);
}
public void delete(Integer id) {
	 repository.deleteById(id);
}
}
