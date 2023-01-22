package com.restservice.profitsoftlec911.repositories;

import com.restservice.profitsoftlec911.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
