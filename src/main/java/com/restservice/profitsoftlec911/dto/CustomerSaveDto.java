package com.restservice.profitsoftlec911.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerSaveDto {

  @NotBlank(message = "name is required")
  private String name;

  @NotBlank(message = "surname is required")
  private String surname;

  private String patronymic;
}
