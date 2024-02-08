package com.loctran.demo.mapper;

import com.loctran.demo.dto.CourseDto;
import com.loctran.demo.entity.Course;
public class CourseMapper {

public static Course mapToEntity(Course entity,CourseDto dto) {
entity.setId(dto.getId());
entity.setName(dto.getName());
return entity;
}
}
