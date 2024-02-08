package com.example.demo.mapper;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
public class UserMapper {

public static User mapToEntity(User entity,UserDto dto) {
entity.setId(dto.getId());
entity.setFirstName(dto.getFirstName());
return entity;
}
}
