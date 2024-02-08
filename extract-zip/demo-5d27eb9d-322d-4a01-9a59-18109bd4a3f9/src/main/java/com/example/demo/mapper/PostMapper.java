package com.example.demo.mapper;

import com.example.demo.dto.PostDto;
import com.example.demo.entity.Post;
public class PostMapper {

public static Post mapToEntity(Post entity,PostDto dto) {
entity.setId(dto.getId());
return entity;
}
}
