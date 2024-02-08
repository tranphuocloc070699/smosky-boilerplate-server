package com.loctran.abc.mapper;

import com.loctran.abc.dto.PostDto;
import com.loctran.abc.entity.Post;
public class PostMapper {

public static Post mapToEntity(Post entity,PostDto dto) {
entity.setId(dto.getId());
entity.setName(dto.getName());
return entity;
}
}
