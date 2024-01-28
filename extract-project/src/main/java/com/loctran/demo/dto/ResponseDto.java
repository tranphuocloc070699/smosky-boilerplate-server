package com.loctran.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class ResponseDto {
private HttpStatus status;
private Object data;
private Object errors;
private String message;
private Date timestamp;
private String path;
}
