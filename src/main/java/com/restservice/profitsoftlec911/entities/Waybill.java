package com.restservice.profitsoftlec911.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "waybills")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "customer")
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Waybill {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NotBlank(message = "type is required")
  private String type;

  @DecimalMin(value = "0.00", message = "price must be provided")
  private Double price;

  @NotNull(message = "Date is required")
  private LocalDate date;

  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
//  @JsonManagedReference("customer")
  private Customer customer;

}
