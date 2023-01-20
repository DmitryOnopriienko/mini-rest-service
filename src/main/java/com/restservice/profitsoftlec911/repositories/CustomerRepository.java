package com.restservice.profitsoftlec911.repositories;

import com.restservice.profitsoftlec911.entities.Customer;
import com.restservice.profitsoftlec911.entities.Waybill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

  @Deprecated
  @Query("""
          SELECT waybill
          FROM Waybill waybill
          WHERE waybill.customer.id = :id
          """)
  List<Waybill> getWaybillsOfCustomer(int id);

}
