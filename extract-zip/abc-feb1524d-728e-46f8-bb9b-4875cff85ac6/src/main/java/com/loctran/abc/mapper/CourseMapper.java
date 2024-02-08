package com.loctran.abc.mapper;

import com.loctran.abc.dto.CourseDto;
import com.loctran.abc.entity.Course;
public class CourseMapper {

public static Course mapToEntity(Course entity,CourseDto dto) {
entity.setId(dto.getId());
entity.setName(dto.getName());
return entity;
}
}
