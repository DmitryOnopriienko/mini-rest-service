package com.restservice.profitsoftlec911;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restservice.profitsoftlec911.entities.Customer;
import com.restservice.profitsoftlec911.repositories.CustomerRepository;
import com.restservice.profitsoftlec911.repositories.WaybillRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WaybillControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private WaybillRepository waybillRepository;

  @Autowired
  private ObjectMapper objectMapper;

  int addCustomer() {
    Customer customer = new Customer();
    customer.setName("TestCustomer");
    customer.setSurname("TestCustomer");
    customer.setPatronymic("TestCustomer");
    customerRepository.save(customer);
    return customer.getId();
  }

  @AfterEach
  void clearRepository() {
    waybillRepository.deleteAll();
  }

  @Test
  void testCreateInvalidWaybills() throws Exception {
    addCustomer();

    mvc.perform(post(WaybillControllerLinksConstants.WAYBILL_CREATE)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {
                        "price": 70.99,
                        "date": "2022-07-07",
                        "customerId": 1
                    }
                    """))
            .andExpect(status().isBadRequest());

    mvc.perform(post(WaybillControllerLinksConstants.WAYBILL_CREATE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "type": "air-drop",
                        "date": "2022-07-07",
                        "customerId": 1
                    }
                    """))
            .andExpect(status().isBadRequest());

    mvc.perform(post(WaybillControllerLinksConstants.WAYBILL_CREATE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "type": "air-drop",
                        "price": 70.99,
                        "customerId": 1
                    }
                    """))
            .andExpect(status().isBadRequest());

    mvc.perform(post(WaybillControllerLinksConstants.WAYBILL_CREATE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "type": "air-drop",
                        "price": 70.99,
                        "date": "2022-07-07"
                    }
                    """))
            .andExpect(status().isBadRequest());

    mvc.perform(post(WaybillControllerLinksConstants.WAYBILL_CREATE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "type": "air-drop",
                        "price": 70.99,
                        "date": "2022-07-07",
                        "customerId": -1
                    }
                    """))
            .andExpect(status().isBadRequest());
  }

  @Test
  void testCreateWaybillWithNonExistentCustomer() throws Exception {
    int maxCustomerId = addCustomer();
    mvc.perform(post(WaybillControllerLinksConstants.WAYBILL_CREATE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "type": "air-drop",
                        "price": 70.99,
                        "date": "2022-07-07",
                        "customerId": %d
                    }
                    """.formatted(maxCustomerId + 1)))
            .andExpect(status().isNotFound());
  }

  private <T>T parseResponse(MvcResult mvcResult, Class<T> c) {
    try {
      return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), c);
    } catch (JsonProcessingException | UnsupportedEncodingException e) {
      throw new RuntimeException("Error parsing json", e);
    }
  }
}
