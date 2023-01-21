package com.restservice.profitsoftlec911.services;

import com.restservice.profitsoftlec911.dto.WaybillQueryDto;
import com.restservice.profitsoftlec911.dto.WaybillSaveDto;
import com.restservice.profitsoftlec911.entities.Customer;
import com.restservice.profitsoftlec911.entities.Waybill;
import com.restservice.profitsoftlec911.exceptions.NotFoundException;
import com.restservice.profitsoftlec911.repositories.CustomerRepository;
import com.restservice.profitsoftlec911.repositories.WaybillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WaybillService {

  private final WaybillRepository waybillRepository;

  private final CustomerRepository customerRepository;

  @Autowired
  public WaybillService(WaybillRepository waybillRepository, CustomerRepository customerRepository) {
    this.waybillRepository = waybillRepository;
    this.customerRepository = customerRepository;
  }

  public Page<Waybill> getAll(Pageable pageable) {
    return waybillRepository.findAll(pageable);
  }

  public Waybill getById(int id) {
    return waybillRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Waybill with id %d not found".formatted(id)));
  }

  public int save(WaybillSaveDto saveDto) {
    Waybill waybill = new Waybill();
    setWaybillData(saveDto, waybill);
    return waybill.getId();
  }

  public void update(int id, WaybillSaveDto saveDto) {
    Waybill waybill = getById(id);
    setWaybillData(saveDto, waybill);
  }

  private void setWaybillData(WaybillSaveDto saveDto, Waybill waybill) {
    int customer_id = saveDto.getCustomerId();
    Customer customer = customerRepository.findById(customer_id)
            .orElseThrow(() ->
                    new NotFoundException("Customer with id %d not found".formatted(customer_id)));
    waybill.setCustomer(customer);

    waybill.setType(saveDto.getType());
    waybill.setPrice(saveDto.getPrice());
    waybill.setDate(saveDto.getDate());
    waybillRepository.save(waybill);
  }

  public void delete(int id) {
    waybillRepository.deleteById(id);
  }

  public Page<Waybill> searchByParameters(WaybillQueryDto queryDto, Pageable pageable) {
    if (queryDto.getDate() != null) {
      return waybillRepository.findByTypeContainingIgnoreCaseAndDate(queryDto.getType(),
              queryDto.getDate(), pageable);
    }
    return waybillRepository.findByTypeContainingIgnoreCase(queryDto.getType(), pageable);
  }
}
