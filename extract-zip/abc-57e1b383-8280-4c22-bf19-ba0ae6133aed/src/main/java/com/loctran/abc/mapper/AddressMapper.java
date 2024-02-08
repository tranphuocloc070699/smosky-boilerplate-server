package com.loctran.abc.mapper;

import com.loctran.abc.dto.AddressDto;
import com.loctran.abc.entity.Address;
public class AddressMapper {

public static Address mapToEntity(Address entity,AddressDto dto) {
entity.setId(dto.getId());
entity.setLocation(dto.getLocation());
return entity;
}
}
