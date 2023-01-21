package com.restservice.profitsoftlec911.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
public class WaybillSaveDto {

  @NotBlank(message = "type is required")
  private String type;

  @NotNull
  @DecimalMin(value = "0.00", message = "price must be provided")
  private Double price;

  @NotNull(message = "Date is required")
  private LocalDate date;

  @NotNull
  @Min(value = 1, message = "customerId must be valid")
  private Integer customerId;

}
