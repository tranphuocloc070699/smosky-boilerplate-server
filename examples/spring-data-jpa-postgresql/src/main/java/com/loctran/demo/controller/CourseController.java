package com.loctran.demo.controller;

import com.loctran.demo.entity.Course;
import com.loctran.demo.service.CourseService;
import com.loctran.demo.dto.CourseDto;
import com.loctran.demo.dto.ResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/courses")
public class CourseController {

private final CourseService service;
private final HttpServletRequest httpServletRequest;

	@GetMapping("")
	 public Object fetchEntities(@RequestParam(value = "current_page", defaultValue = "1", required = false) int currentPage,
	 @RequestParam(value = "page_size", defaultValue = "10") int pageSize,
	 @RequestParam(value = "sort_by", defaultValue = "id") String sortBy,
	 @RequestParam(value = "sort_dir", defaultValue = "desc", required = false) String sortDir) {

	 	 	Page<Course> entities = service.fetchEntities(currentPage, pageSize, sortBy, sortDir);
	 	 	ResponseDto responseDto = ResponseDto.builder()
	 	 	.timestamp(new Date())
	 	 	.status(HttpStatus.CREATED)
	 	 	.path(httpServletRequest.getContextPath())
	 	 	.data(entities)
	 	 	.errors(null)
	 	 	.message("Process Successfully!")
	 	 	.build();
	 	 	return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	 	}
	@GetMapping("{id}")
	 public Object fetchDetails(@RequestParam(value = "id") Integer id) {
	 	Course entity = service.fetchEntity(id);
	 	 	ResponseDto responseDto = ResponseDto.builder()
	 	 	.timestamp(new Date())
	 	 	.status(HttpStatus.CREATED)
	 	 	.path(httpServletRequest.getContextPath())
	 	 	.data(entity)
	 	 	.errors(null)
	 	 	.message("Process Successfully!")
	 	 	.build();
	 	 	return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	 	}
	@PostMapping("")
	 public Object createEntity(@RequestBody CourseDto dto) {
	 	Course entity = service.createEntity(dto);
	 	 	ResponseDto responseDto = ResponseDto.builder()
	 	 	.timestamp(new Date())
	 	 	.status(HttpStatus.CREATED)
	 	 	.path(httpServletRequest.getContextPath())
	 	 	.data(entity)
	 	 	.errors(null)
	 	 	.message("Process Successfully!")
	 	 	.build();
	 	 	return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	 	}
	@PutMapping("{id}")
	 public Object updateEntity(@PathVariable("id")Integer id,@RequestBody CourseDto dto) {
	 	Course entity = service.updateEntity(dto,id);
	 	 	ResponseDto responseDto = ResponseDto.builder()
	 	 	.timestamp(new Date())
	 	 	.status(HttpStatus.OK)
	 	 	.path(httpServletRequest.getContextPath())
	 	 	.data(entity)
	 	 	.errors(null)
	 	 	.message("Process Successfully!")
	 	 	.build();
	 	 	return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	 	}
	@DeleteMapping("{id}")
	 public Object deleteEntity(@PathVariable("id")Integer id) {
	 	Course entity = service.deleteEntity(id);
	 	 	ResponseDto responseDto = ResponseDto.builder()
	 	 	.timestamp(new Date())
	 	 	.status(HttpStatus.OK)
	 	 	.path(httpServletRequest.getContextPath())
	 	 	.data(entity)
	 	 	.errors(null)
	 	 	.message("Process Successfully!")
	 	 	.build();
	 	 	return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	 	}
	@PatchMapping("/add/{id}")
	 public Object addUser(@PathVariable("id")Integer id,@RequestParam("userId") String userId) {
	 	Course entity = service.addUser(id,Integer.parseInt(userId));
	 	 	ResponseDto responseDto = ResponseDto.builder()
	 	 	.timestamp(new Date())
	 	 	.status(HttpStatus.OK)
	 	 	.path(httpServletRequest.getContextPath())
	 	 	.data(entity)
	 	 	.errors(null)
	 	 	.message("Process Successfully!")
	 	 	.build();
	 	 	return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	 	}
	@PatchMapping("/remove/{id}")
	 public Object removeUser(@PathVariable("id")Integer id,@RequestParam("userId") String userId) {
	 	Course entity = service.removeUser(id,Integer.parseInt(userId));
	 	 	ResponseDto responseDto = ResponseDto.builder()
	 	 	.timestamp(new Date())
	 	 	.status(HttpStatus.OK)
	 	 	.path(httpServletRequest.getContextPath())
	 	 	.data(entity)
	 	 	.errors(null)
	 	 	.message("Process Successfully!")
	 	 	.build();
	 	 	return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	 	}
}
