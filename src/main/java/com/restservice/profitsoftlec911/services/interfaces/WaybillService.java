package com.restservice.profitsoftlec911.services.interfaces;

import com.restservice.profitsoftlec911.dto.WaybillQueryDto;
import com.restservice.profitsoftlec911.dto.WaybillSaveDto;
import com.restservice.profitsoftlec911.entities.Waybill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WaybillService {

  Page<Waybill> getAll(Pageable pageable);

  Waybill getById(int id);

  int save(WaybillSaveDto saveDto);

  void update(int id, WaybillSaveDto saveDto);

  void delete(int id);

  Page<Waybill> searchByParameters(WaybillQueryDto queryDto, Pageable pageable);
}

