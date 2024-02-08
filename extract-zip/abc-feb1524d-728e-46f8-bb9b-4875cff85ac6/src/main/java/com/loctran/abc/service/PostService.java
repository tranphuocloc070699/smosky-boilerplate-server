package com.loctran.abc.service;

import com.loctran.abc.entity.Post;
import com.loctran.abc.dao.PostDataAccess;
import com.loctran.abc.mapper.PostMapper;
import com.loctran.abc.dto.PostDto;

import com.loctran.abc.entity.User;
import com.loctran.abc.dao.UserDataAccess;

import com.loctran.abc.exception.ConflictException;
import com.loctran.abc.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
	 private final PostDataAccess dataAccess;
	 private final UserDataAccess userDataAccess;

	public Post createEntity(Integer userId,PostDto dto) {
	 	 Post entity = Post.builder().build();
	 	 PostMapper.mapToEntity(entity, dto);
	 	User user = userDataAccess.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","userId",userId.toString()));
	 	entity.setUser(user);
	 	return dataAccess.save(entity);
	}

	 public Post fetchEntity(Integer id) {
	 	 return dataAccess.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","Id",id.toString()));
	}
	 public Page<Post> fetchEntities(int currentPage,int pageSize ,String sortBy, String sortDir) {
	 	 Sort sort = Sort.by(sortBy);
	 	 sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
	 	 Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);

	 	 return dataAccess.findAll(pageable);
	}
	  public Post updateEntity(PostDto dto,Integer id) {
	 	 boolean isUpdated = false;
	 	 Post modelExisted = dataAccess.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","Id",id.toString()));

	 	 	if (!modelExisted.getId().equals(dto.getId())) {
	 	 	modelExisted.setId(dto.getId());
	 	 	isUpdated=true;
	 	}
	 	 	if (!modelExisted.getName().equals(dto.getName())) {
	 	 	modelExisted.setName(dto.getName());
	 	 	isUpdated=true;
	 	}
	 	 if(!isUpdated) throw new ConflictException("Property not changing");
	 	 dataAccess.save(modelExisted);
	 	 return modelExisted;
	}

	  public Post deleteEntity(Integer id) {
	 	 Post modelExisted = dataAccess.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","Id",id.toString()));
	 	dataAccess.delete(id);
	 	return modelExisted;
	};

}
