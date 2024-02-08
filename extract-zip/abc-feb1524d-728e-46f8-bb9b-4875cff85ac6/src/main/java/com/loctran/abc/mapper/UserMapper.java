package com.loctran.abc.mapper;

import com.loctran.abc.dto.UserDto;
import com.loctran.abc.entity.User;
public class UserMapper {

public static User mapToEntity(User entity,UserDto dto) {
entity.setId(dto.getId());
entity.setFirstName(dto.getFirstName());
entity.setLastName(dto.getLastName());
return entity;
}
}
