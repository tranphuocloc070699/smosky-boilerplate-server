package com.loctran.demo.mapper;

import com.loctran.demo.dto.AddressDto;
import com.loctran.demo.entity.Address;
public class AddressMapper {

public static Address mapToEntity(Address entity,AddressDto dto) {
entity.setId(dto.getId());
entity.setLocation(dto.getLocation());
return entity;
}
}
