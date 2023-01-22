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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "waybill")
@Getter
@Setter
@NoArgsConstructor
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Waybill {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String type;

  private Double price;

  private LocalDate date;

  @ManyToOne(optional = false)
  @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
  private Customer customer;

}
