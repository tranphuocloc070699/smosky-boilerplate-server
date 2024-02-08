package com.loctran.demo.mapper;

import com.loctran.demo.dto.PostDto;
import com.loctran.demo.entity.Post;
public class PostMapper {

public static Post mapToEntity(Post entity,PostDto dto) {
entity.setId(dto.getId());
entity.setName(dto.getName());
return entity;
}
}
