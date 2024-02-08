package com.loctran.abc.exception;;import com.loctran.abc.dto.ResponseDto;



import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
      WebRequest request) {
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
        .timestamp(new Date())
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
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
        .timestamp(new Date())
        .status(HttpStatus.NOT_FOUND)
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
        .timestamp(new Date())
        .status(HttpStatus.CONFLICT)
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
        .timestamp(new Date())
        .status(HttpStatus.BAD_REQUEST)
        .message("Process failure!")
        .errors(exception.getMessage())
        .data(null)
        .build();
    return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
  }

}
