package com.restservice.profitsoftlec911.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WaybillQueryDto {

  private String type = "";

  private LocalDate date;

}
