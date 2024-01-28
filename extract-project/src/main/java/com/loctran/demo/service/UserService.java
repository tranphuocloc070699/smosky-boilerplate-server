package com.loctran.demo.service;

import com.loctran.demo.entity.User;
import com.loctran.demo.dao.UserDataAccess;
import com.loctran.demo.mapper.UserMapper;
import com.loctran.demo.dto.UserDto;

import com.loctran.demo.entity.Address;
import com.loctran.demo.dao.AddressDataAccess;

import com.loctran.demo.entity.Post;
import com.loctran.demo.dao.PostDataAccess;

import com.loctran.demo.entity.Course;
import com.loctran.demo.dao.CourseDataAccess;

import com.loctran.demo.exception.ConflictException;
import com.loctran.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
	 private final UserDataAccess dataAccess;
	 private final AddressDataAccess addressDataAccess;
	 private final PostDataAccess postDataAccess;
	 private final CourseDataAccess courseDataAccess;

	public User createEntity(UserDto dto) {
	 	 User entity = User.builder().build();
	 	 UserMapper.mapToEntity(entity, dto);
	 	return dataAccess.save(entity);
	}

	 public User fetchEntity(Integer id) {
	 	 return dataAccess.findById(id).orElseThrow(() -> new ResourceNotFoundException("User","Id",id.toString()));
	}
	 public Page<User> fetchEntities(int currentPage,int pageSize ,String sortBy, String sortDir) {
	 	 Sort sort = Sort.by(sortBy);
	 	 sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
	 	 Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);

	 	 return dataAccess.findAll(pageable);
	}
	  public User updateEntity(UserDto dto,Integer id) {
	 	 boolean isUpdated = false;
	 	 User modelExisted = dataAccess.findById(id).orElseThrow(() -> new ResourceNotFoundException("User","Id",id.toString()));

	 	 	if (!modelExisted.getId().equals(dto.getId())) {
	 	 	modelExisted.setId(dto.getId());
	 	 	isUpdated=true;
	 	}
	 	 	if (!modelExisted.getFirstName().equals(dto.getFirstName())) {
	 	 	modelExisted.setFirstName(dto.getFirstName());
	 	 	isUpdated=true;
	 	}
	 	 	if (!modelExisted.getLastName().equals(dto.getLastName())) {
	 	 	modelExisted.setLastName(dto.getLastName());
	 	 	isUpdated=true;
	 	}
	 	 if(!isUpdated) throw new ConflictException("Property not changing");
	 	 dataAccess.save(modelExisted);
	 	 return modelExisted;
	}

	  public User deleteEntity(Integer id) {
	 	 User modelExisted = dataAccess.findById(id).orElseThrow(() -> new ResourceNotFoundException("User","Id",id.toString()));
	 	dataAccess.delete(id);
	 	return modelExisted;
	};

}
