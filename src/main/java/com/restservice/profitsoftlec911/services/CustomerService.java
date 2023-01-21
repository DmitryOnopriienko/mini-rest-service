package com.restservice.profitsoftlec911.services;

import com.restservice.profitsoftlec911.entities.Customer;
import com.restservice.profitsoftlec911.entities.Waybill;
import com.restservice.profitsoftlec911.exceptions.NotFoundException;
import com.restservice.profitsoftlec911.repositories.CustomerRepository;
import com.restservice.profitsoftlec911.repositories.WaybillRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class CustomerService {

  private final CustomerRepository customerRepository;

  private final WaybillRepository waybillRepository;

  @Autowired
  public CustomerService(CustomerRepository customerRepository, WaybillRepository waybillRepository) {
    this.customerRepository = customerRepository;
    this.waybillRepository = waybillRepository;
  }

  public Customer getById(int id) {
    return customerRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Customer with id %d not found".formatted(id)));
  }

  public int save(Customer customer) {
    customerRepository.save(customer);
    return customer.getId();
  }

  public Page<Customer> getAll(Pageable pageable) {
    return customerRepository.findAll(pageable);
  }

  public Page<Waybill> getWaybillsOfCustomer(int id, Pageable pageable) {
    return waybillRepository.findByCustomerId(id, pageable);
  }
}
