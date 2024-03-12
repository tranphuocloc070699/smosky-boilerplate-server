package com.smosky.boilerplateserver.blog;

import com.smosky.boilerplateserver.blog.dtos.UpSavePostDto;
import com.smosky.boilerplateserver.shared.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("posts")
@RequiredArgsConstructor
public class BlogController {
  private final BlogService blogService;


  @CrossOrigin(origins = "http://localhost:8000")
  @GetMapping("")
  ResponseEntity<ResponseDto> fetchAllPost(){
    return blogService.fetchAllPost();
  }

  @CrossOrigin(origins = "http://localhost:8000")
  @GetMapping("{slug}")
  ResponseEntity<ResponseDto> fetchPost(@PathVariable("slug") String slug){
    return blogService.fetchPost(slug);
  }

  @CrossOrigin(origins = "http://localhost:8000")
  @PostMapping("")
  ResponseEntity<ResponseDto> createPost(@RequestBody UpSavePostDto dto){
    return blogService.createPost(dto);
  }

  @CrossOrigin(origins = "http://localhost:8000")
  @PutMapping("{id}")
  ResponseEntity<ResponseDto> updatePost(@PathVariable("id")String id,@RequestBody UpSavePostDto dto){
    return blogService.updatePost(Integer.parseInt(id),dto);
  }

  @CrossOrigin(origins = "http://localhost:8000")
  @DeleteMapping("{id}")
  ResponseEntity<ResponseDto> deletePost(@PathVariable("id") String id){
    return blogService.deletePost(Integer.parseInt(id));
  }
}
