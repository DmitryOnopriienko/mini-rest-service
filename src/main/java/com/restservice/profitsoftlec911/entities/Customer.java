package com.restservice.profitsoftlec911.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NotBlank(message = "name is required")
  private String name;

  @NotBlank(message = "surname is required")
  private String surname;

  private String patronymic;

//  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
////  @JsonBackReference("waybills")
//  @JsonIgnore
//  List<Waybill> waybills;

}
