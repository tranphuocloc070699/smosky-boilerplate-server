package com.loctran.demo.mapper;

import com.loctran.demo.dto.UserDto;
import com.loctran.demo.entity.User;
public class UserMapper {

public static User mapToEntity(User entity,UserDto dto) {
entity.setId(dto.getId());
entity.setFirstName(dto.getFirstName());
entity.setLastName(dto.getLastName());
return entity;
}
}
