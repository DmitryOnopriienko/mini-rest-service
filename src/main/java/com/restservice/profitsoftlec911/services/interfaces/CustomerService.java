package com.restservice.profitsoftlec911.services.interfaces;

import com.restservice.profitsoftlec911.dto.CustomerSaveDto;
import com.restservice.profitsoftlec911.entities.Customer;
import com.restservice.profitsoftlec911.entities.Waybill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

  Customer getById(int id);

  int save(CustomerSaveDto saveDto);

  void update(CustomerSaveDto customer, int id);

  Page<Customer> getAll(Pageable pageable);

  Page<Waybill> getWaybillsOfCustomer(int id, Pageable pageable);

  void delete(int id);
}
