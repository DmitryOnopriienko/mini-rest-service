package com.restservice.profitsoftlec911.repositories;

import com.restservice.profitsoftlec911.entities.Waybill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface WaybillRepository extends JpaRepository<Waybill, Integer> {

  Page<Waybill> findByTypeContainingIgnoreCaseAndDate(String type, LocalDate date, Pageable pageable);

  Page<Waybill> findByTypeContainingIgnoreCase(String type, Pageable pageable);

  Page<Waybill> findByCustomerId(int customerId, Pageable pageable);
}
