package com.smosky.boilerplateserver.spring;

import com.smosky.boilerplateserver.shared.AppInfoConfigDto;
import com.smosky.boilerplateserver.shared.Constant;
import com.smosky.boilerplateserver.shared.FileService;
import com.smosky.boilerplateserver.spring.dtos.BoilerplateDetailDto;
import com.smosky.boilerplateserver.spring.dtos.CreateBoilerplateDto;
import com.smosky.boilerplateserver.spring.dtos.CreateEntityDependencyId;
import com.smosky.boilerplateserver.spring.dtos.CreateReviewDto;
import com.smosky.boilerplateserver.spring.dtos.CreateReviewResponseDto;
import com.smosky.boilerplateserver.spring.dtos.DependencyDto;
import com.smosky.boilerplateserver.spring.dtos.EntityDto;
import com.smosky.boilerplateserver.spring.dtos.MetadataDto;
import com.smosky.boilerplateserver.spring.dtos.StarCountDto;
import com.smosky.boilerplateserver.spring.dtos.TemplateDto;
import com.smosky.boilerplateserver.util.ApplicationFileTemplate;
import com.smosky.boilerplateserver.util.CustomWriter;
import com.smosky.boilerplateserver.util.DaoTemplate;
import com.smosky.boilerplateserver.util.DtoTemplate;
import com.smosky.boilerplateserver.util.EntityTemplate;
import com.smosky.boilerplateserver.util.ExceptionTemplate;
import com.smosky.boilerplateserver.util.MapperTemplate;
import com.smosky.boilerplateserver.util.RepositoryTemplate;
import com.smosky.boilerplateserver.util.ServiceTemplate;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;


@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000","https://softsky.dev","https://www.softsky.dev"})
public class ReviewController {
  private final ReviewRepository reviewRepository;
  private final BoilerplateRepository boilerplateRepository;

  @PostMapping("")
  @CrossOrigin(origins = {"http://localhost:3000","https://softsky.dev","https://www.softsky.dev"})
  public Object createReview(@RequestBody CreateReviewDto dto) {
    System.out.println(dto.getBoilerplateId());
    if (dto.getName().isEmpty() || dto.getBoilerplateId() == null) {
      return null;
    }
    if (dto.getEmail() == null) {
      dto.setEmail("anomyous@example.com");
    }

    Boilerplate boilerplateExisting = boilerplateRepository.findById(dto.getBoilerplateId()).orElseThrow(() -> new RuntimeException("Boilerplate not found"));
    Review review = reviewRepository.save(Review.builder()
        .name(dto.getName())
        .star(dto.getStar())
        .content(dto.getContent())
        .email(dto.getEmail())
        .boilerplate(boilerplateExisting)
        .build());

    StarCountDto starCountDto = boilerplateRepository.findStarCounts(dto.getBoilerplateId());

    CreateReviewResponseDto createReviewResponseDto = CreateReviewResponseDto.builder()
        .id(review.getId())
        .name(review.getName())
        .email(review.getEmail())
        .content(review.getContent())
        .star(review.getStar())
        .createdAt(review.getCreatedAt())
        .updatedAt(review.getUpdatedAt())
        .totalReview(starCountDto.getTotalReviews())
        .starAvg(starCountDto.getStarAvg())
        .oneStar(starCountDto.getOneStarCount())
        .twoStar(starCountDto.getTwoStarCount())
        .threeStar(starCountDto.getThreeStarCount())
        .fourStar(starCountDto.getFourStarCount())
        .fiveStar(starCountDto.getFiveStarCount())
        .build();

    return createReviewResponseDto;
  }

  @PostMapping("{id}")
  @CrossOrigin(origins = {"http://localhost:3000","https://softsky.dev","https://www.softsky.dev"})
  public Object deleteReview(@PathVariable("id")String id) {
    Integer idConverter = Integer.parseInt(id);

    Review reviewExisting = reviewRepository.findById(idConverter).orElseThrow(() -> new RuntimeException("Review not found"));
    reviewRepository.delete(reviewExisting);
    return true;
  }
}
