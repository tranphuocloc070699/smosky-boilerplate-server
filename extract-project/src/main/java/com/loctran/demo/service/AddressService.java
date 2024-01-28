package com.loctran.demo.service;

import com.loctran.demo.entity.Address;
import com.loctran.demo.dao.AddressDataAccess;
import com.loctran.demo.mapper.AddressMapper;
import com.loctran.demo.dto.AddressDto;

import com.loctran.demo.entity.User;
import com.loctran.demo.dao.UserDataAccess;

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
public class AddressService {
	 private final AddressDataAccess dataAccess;
	 private final UserDataAccess userDataAccess;

	public Address createEntity(Integer userId,AddressDto dto) {
	 	 Address entity = Address.builder().build();
	 	 AddressMapper.mapToEntity(entity, dto);
	 	User user = userDataAccess.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","userId",userId.toString()));
	 	entity.setUser(user);
	 	return dataAccess.save(entity);
	}

	 public Address fetchEntity(Integer id) {
	 	 return dataAccess.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address","Id",id.toString()));
	}
	 public Page<Address> fetchEntities(int currentPage,int pageSize ,String sortBy, String sortDir) {
	 	 Sort sort = Sort.by(sortBy);
	 	 sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
	 	 Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);

	 	 return dataAccess.findAll(pageable);
	}
	  public Address updateEntity(AddressDto dto,Integer id) {
	 	 boolean isUpdated = false;
	 	 Address modelExisted = dataAccess.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address","Id",id.toString()));

	 	 	if (!modelExisted.getId().equals(dto.getId())) {
	 	 	modelExisted.setId(dto.getId());
	 	 	isUpdated=true;
	 	}
	 	 	if (!modelExisted.getLocation().equals(dto.getLocation())) {
	 	 	modelExisted.setLocation(dto.getLocation());
	 	 	isUpdated=true;
	 	}
	 	 if(!isUpdated) throw new ConflictException("Property not changing");
	 	 dataAccess.save(modelExisted);
	 	 return modelExisted;
	}

	  public Address deleteEntity(Integer id) {
	 	 Address modelExisted = dataAccess.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address","Id",id.toString()));
	 	dataAccess.delete(id);
	 	return modelExisted;
	};

}
