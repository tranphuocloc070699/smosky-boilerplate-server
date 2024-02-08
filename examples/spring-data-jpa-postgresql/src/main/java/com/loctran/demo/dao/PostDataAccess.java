package com.loctran.demo.dao;

import com.loctran.demo.entity.Post;
import com.loctran.demo.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostDataAccess {

private final PostRepository repository;

public Optional<Post> findById(Integer id) {
	 return repository.findById(id);
}
public Post save(Post entity) {
	 return repository.save(entity);
}
public Page<Post> findAll(Pageable pageable) {
	 return repository.findAll(pageable);
}
public void delete(Integer id) {
	 repository.deleteById(id);
}
}
