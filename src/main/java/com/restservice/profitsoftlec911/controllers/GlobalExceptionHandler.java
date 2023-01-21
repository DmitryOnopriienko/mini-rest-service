package com.restservice.profitsoftlec911.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.restservice.profitsoftlec911.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  protected @ResponseBody ErrorResponse handleNotFound(NotFoundException e) {
    return new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            e.getMessage());
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  static class ErrorResponse {
    private int status;
    private String error;
    private String message;
  }

}
