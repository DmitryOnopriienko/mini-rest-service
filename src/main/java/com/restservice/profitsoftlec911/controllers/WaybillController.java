package com.restservice.profitsoftlec911.controllers;

import com.restservice.profitsoftlec911.dto.RestResponse;
import com.restservice.profitsoftlec911.dto.WaybillQueryDto;
import com.restservice.profitsoftlec911.dto.WaybillSaveDto;
import com.restservice.profitsoftlec911.entities.Waybill;
import com.restservice.profitsoftlec911.services.interfaces.WaybillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/waybills")
public class WaybillController {

  private final WaybillService waybillService;

  @Autowired
  public WaybillController(WaybillService waybillService) {
    this.waybillService = waybillService;
  }

  @GetMapping
  public Page<Waybill> getAll(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "3") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return waybillService.getAll(pageable);
  }

  @GetMapping("/{id}")
  public Waybill getById(@PathVariable("id") int id) {
    return waybillService.getById(id);
  }

  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  public RestResponse create(@Valid @RequestBody WaybillSaveDto waybill) {
    int id = waybillService.save(waybill);
    return new RestResponse(String.valueOf(id));
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") int id) {
    waybillService.delete(id);
  }

  @PatchMapping("/{id}")
  public RestResponse update(@PathVariable("id") int id, @Valid @RequestBody WaybillSaveDto waybill) {
    waybillService.update(id, waybill);
    return new RestResponse("OK");
  }

  @PostMapping("/_search")
  public Page<Waybill> searchByParams(@RequestBody WaybillQueryDto queryDto,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "3") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return waybillService.searchByParameters(queryDto, pageable);
  }

}
