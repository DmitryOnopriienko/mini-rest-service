package com.restservice.profitsoftlec911.controllers;

import com.restservice.profitsoftlec911.dto.CustomerSaveDto;
import com.restservice.profitsoftlec911.dto.RestResponse;
import com.restservice.profitsoftlec911.entities.Customer;
import com.restservice.profitsoftlec911.entities.Waybill;
import com.restservice.profitsoftlec911.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

  private final CustomerService customerService;

  @Autowired
  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @GetMapping
  public Page<Customer> getAll(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "3") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return customerService.getAll(pageable);
  }

  @GetMapping("/{id}")
  public Customer getById(@PathVariable("id") int id) {
    return customerService.getById(id);
  }

  @GetMapping("/{id}/waybills")
  public Page<Waybill> getWaybillsOfCustomer(@PathVariable("id") int id,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "3") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return customerService.getWaybillsOfCustomer(id, pageable);
  }

  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  public RestResponse createCustomer(@Valid @RequestBody CustomerSaveDto customer) {
    int id = customerService.save(customer);
    return new RestResponse(String.valueOf(id));
  }
}
