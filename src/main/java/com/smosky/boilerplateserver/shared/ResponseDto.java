package com.smosky.boilerplateserver.shared;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;


@Data
@AllArgsConstructor
@Builder
public class ResponseDto {
  private Integer status;
  private Object data;
  private Object errors;
  private String message;
  private String path;
}
