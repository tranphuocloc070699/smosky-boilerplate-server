package com.loctran.demo.controller;

import com.loctran.demo.entity.Post;
import com.loctran.demo.service.PostService;
import com.loctran.demo.dto.PostDto;
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
@RequestMapping("api/posts")
public class PostController {

private final PostService service;
private final HttpServletRequest httpServletRequest;

	@GetMapping("")
	 public Object fetchEntities(@RequestParam(value = "current_page", defaultValue = "1", required = false) int currentPage,
	 @RequestParam(value = "page_size", defaultValue = "10") int pageSize,
	 @RequestParam(value = "sort_by", defaultValue = "id") String sortBy,
	 @RequestParam(value = "sort_dir", defaultValue = "desc", required = false) String sortDir) {

	 	 	Page<Post> entities = service.fetchEntities(currentPage, pageSize, sortBy, sortDir);
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
	 	Post entity = service.fetchEntity(id);
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
	 public Object createEntity(@RequestParam("userId") Integer userId,@RequestBody PostDto dto) {
	 	Post entity = service.createEntity(userId,dto);
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
	 public Object updateEntity(@PathVariable("id")Integer id,@RequestBody PostDto dto) {
	 	Post entity = service.updateEntity(dto,id);
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
	 	Post entity = service.deleteEntity(id);
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
