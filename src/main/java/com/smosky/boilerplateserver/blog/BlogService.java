package com.smosky.boilerplateserver.blog;

import com.smosky.boilerplateserver.blog.dtos.TocDto;
import com.smosky.boilerplateserver.blog.dtos.UpSavePostDto;
import com.smosky.boilerplateserver.blog.entity.Post;
import com.smosky.boilerplateserver.blog.entity.Toc;
import com.smosky.boilerplateserver.blog.repository.BlogRepository;
import com.smosky.boilerplateserver.blog.repository.TocRepository;
import com.smosky.boilerplateserver.exception.ConflictException;
import com.smosky.boilerplateserver.exception.ResourceNotFoundException;
import com.smosky.boilerplateserver.shared.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogService {

  private final BlogRepository blogRepository;
  private final HttpServletRequest httpServletRequest;
  private final TocRepository tocRepository;

  public ResponseEntity<ResponseDto> fetchAllPost() {

    List<Post> posts = blogRepository.findAll();

    ResponseDto responseDto = ResponseDto.builder()
        .path(httpServletRequest.getServletPath())
        .status(HttpStatus.OK.value())
        .message("fetch all post successfully!")
        .data(posts)
        .build();

    return ResponseEntity.ok(responseDto);
  }

  public ResponseEntity<ResponseDto> fetchPost(String slug) {

    Post post = blogRepository.findBySlug(slug)
        .orElseThrow(() -> new ResourceNotFoundException("Post", "slug", slug));

    ResponseDto responseDto = ResponseDto.builder()
        .path(httpServletRequest.getServletPath())
        .status(HttpStatus.OK.value())
        .message("fetch post successfully!")
        .data(post)
        .build();

    return ResponseEntity.ok(responseDto);
  }

  public ResponseEntity<ResponseDto> deletePost(Integer id) {
    Post post = blogRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id.toString()));

    blogRepository.delete(post);
    ResponseDto responseDto = ResponseDto.builder()
        .path(httpServletRequest.getServletPath())
        .status(HttpStatus.OK.value())
        .message("delete post successfully!")
        .data(post)
        .build();
    return ResponseEntity.ok(responseDto);
  }

  public ResponseEntity<ResponseDto> createPost(UpSavePostDto dto) {
    Optional<Post> postOptional = blogRepository.findBySlug(dto.getSlug());
    if (postOptional.isPresent()) {
      throw new ConflictException("Post already exists, cannot create");
    }

    Post post = blogRepository.save(Post.builder()
        .title(dto.getTitle())
        .slug(dto.getSlug())
        .content(dto.getContent())
        .preContent(dto.getPreContent())
        .thumbnail(dto.getThumbnail())
        .build());
    System.out.println("toc:" +dto.getToc().size());
    for (TocDto tocDto : dto.getToc()) {
      tocRepository.save(
          Toc.builder().title(tocDto.getTitle()).link(tocDto.getLink()).type(tocDto.getType())
              .post(post).build());
    }

    ResponseDto responseDto = ResponseDto.builder()
        .path(httpServletRequest.getServletPath())
        .status(HttpStatus.OK.value())
        .message("create post successfully!")
        .data(post)
        .build();
    return ResponseEntity.ok(responseDto);
  }

  public ResponseEntity<ResponseDto> updatePost(Integer id, UpSavePostDto dto) {
    Post post = blogRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id.toString()));

    List<String> affects = new ArrayList<>();
    if (!dto.getTitle().equals(post.getTitle())) {
      post.setTitle(dto.getTitle());
      affects.add("Title");
    }
    if (!dto.getThumbnail().equals(post.getThumbnail())) {
      post.setThumbnail(dto.getThumbnail());
      affects.add("Thumbnail");
    }
    if (!dto.getSlug().equals(post.getSlug())) {
      post.setSlug(dto.getSlug());
      affects.add("Slug");
    }
    if (!dto.getPreContent().equals(post.getPreContent())) {
      post.setPreContent(dto.getPreContent());
      affects.add("PreContent");
    }
    if (!dto.getContent().equals(post.getContent())) {
      post.setContent(dto.getContent());
      affects.add("Content");
    }



    Post postUpdated = blogRepository.save(post);

    Map<String, Object> map = new HashMap<>();
    map.put("post", postUpdated);
    map.put("affects", affects);
    ResponseDto responseDto = ResponseDto.builder()
        .path(httpServletRequest.getServletPath())
        .status(HttpStatus.OK.value())
        .message("update post successfully!")
        .data(map)
        .build();
    return ResponseEntity.ok(responseDto);
  }
}
