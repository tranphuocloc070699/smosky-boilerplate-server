package com.smosky.boilerplateserver.exception;

import com.smosky.boilerplateserver.shared.ResponseDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {


  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request)
  {
    Map<String, String> validationErrors = new HashMap<>();
    List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

    validationErrorList.forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String validationMsg = error.getDefaultMessage();
      validationErrors.put(fieldName, validationMsg);
    });
    return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseDto> handleGlobalException(Exception exception,
      WebRequest webRequest) {
    ResponseDto responseDto = ResponseDto.builder()
        .path(webRequest.getContextPath())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .message("Process failure!")
        .errors(exception.getMessage())
        .data(null)
        .build();
    return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ResponseDto> handleResourceNotFoundException(
      ResourceNotFoundException exception,
      WebRequest webRequest) {
    ResponseDto responseDto = ResponseDto.builder()
        .path(webRequest.getContextPath())
        .status(HttpStatus.NOT_FOUND.value())
        .message("Process failure!")
        .errors(exception.getMessage())
        .data(null)
        .build();
    return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ResponseDto> handleConflictException(ConflictException exception,
      WebRequest webRequest) {
    ResponseDto responseDto = ResponseDto.builder()
        .path(webRequest.getContextPath())
        .status(HttpStatus.CONFLICT.value())
        .message("Process failure!")
        .errors(exception.getMessage())
        .data(null)
        .build();
    return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
  }


  @ExceptionHandler(AlreadyExistException.class)
  public ResponseEntity<ResponseDto> handleCustomerAlreadyExistsException(
      AlreadyExistException exception,
      WebRequest webRequest) {
    ResponseDto responseDto = ResponseDto.builder()
        .path(webRequest.getContextPath())
        .status(HttpStatus.BAD_REQUEST.value())
        .message("Process failure!")
        .errors(exception.getMessage())
        .data(null)
        .build();
    return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
  }

}
