package com.example.demo.mapper;

import com.example.demo.dto.AddressDto;
import com.example.demo.entity.Address;
public class AddressMapper {

public static Address mapToEntity(Address entity,AddressDto dto) {
entity.setId(dto.getId());
return entity;
}
}
