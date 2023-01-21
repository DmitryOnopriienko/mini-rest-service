package com.restservice.profitsoftlec911.services;

import com.restservice.profitsoftlec911.dto.WaybillQueryDto;
import com.restservice.profitsoftlec911.entities.Waybill;
import com.restservice.profitsoftlec911.exceptions.NotFoundException;
import com.restservice.profitsoftlec911.repositories.WaybillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WaybillService {

  private final WaybillRepository waybillRepository;

  @Autowired
  public WaybillService(WaybillRepository waybillRepository) {
    this.waybillRepository = waybillRepository;
  }

  public Page<Waybill> getAll(Pageable pageable) {
    return waybillRepository.findAll(pageable);
  }

  public Waybill getById(int id) {
    return waybillRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Waybill with id %d not found".formatted(id)));
  }

  public int save(Waybill waybill) {
    waybillRepository.save(waybill);
    return waybill.getId();
  }

  public void update(int id, Waybill newWaybillInfo) {
    Waybill waybill = getById(id);
    waybill.setPrice(newWaybillInfo.getPrice());
    waybill.setType(newWaybillInfo.getType());
    waybill.setDate(newWaybillInfo.getDate());
    waybill.setCustomer(newWaybillInfo.getCustomer());
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
