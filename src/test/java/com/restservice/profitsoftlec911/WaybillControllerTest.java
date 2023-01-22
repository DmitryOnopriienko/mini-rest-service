package com.restservice.profitsoftlec911;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restservice.profitsoftlec911.dto.RestResponse;
import com.restservice.profitsoftlec911.entities.Customer;
import com.restservice.profitsoftlec911.entities.Waybill;
import com.restservice.profitsoftlec911.exceptions.NotFoundException;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

  public static final String WAYBILLS = "/waybills";

  public static final String WAYBILL_CREATE = WAYBILLS + "/create";

  int addCustomer() {
    Customer customer = new Customer();
    customer.setName("TestCustomer");
    customer.setSurname("TestCustomer");
    customer.setPatronymic("TestCustomer");
    customerRepository.save(customer);
    return customer.getId();
  }

  @AfterEach
  void clearRepositories() {
    waybillRepository.deleteAll();
    customerRepository.deleteAll();
  }

  @Test
  void testCreateInvalidWaybills() throws Exception {
    addCustomer();

    mvc.perform(post(WAYBILL_CREATE)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {
                        "price": 70.99,
                        "date": "2022-07-07",
                        "customerId": 1
                    }
                    """))
            .andExpect(status().isBadRequest());

    mvc.perform(post(WAYBILL_CREATE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "type": "air-drop",
                        "date": "2022-07-07",
                        "customerId": 1
                    }
                    """))
            .andExpect(status().isBadRequest());

    mvc.perform(post(WAYBILL_CREATE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "type": "air-drop",
                        "price": 70.99,
                        "customerId": 1
                    }
                    """))
            .andExpect(status().isBadRequest());

    mvc.perform(post(WAYBILL_CREATE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "type": "air-drop",
                        "price": 70.99,
                        "date": "2022-07-07"
                    }
                    """))
            .andExpect(status().isBadRequest());

    mvc.perform(post(WAYBILL_CREATE)
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
    mvc.perform(post(WAYBILL_CREATE)
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

  @Test
  void testCreateValidWaybill() throws Exception {
    int customerId = addCustomer();

    String type = "air-drop";
    String price = "70.99";
    String date = "2022-07-07";

    MvcResult result = mvc.perform(post(WAYBILL_CREATE)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {
                      "type": "%s",
                      "price": %s,
                      "date": "%s",
                      "customerId": %d
                    }
                    """.formatted(type, price, date, customerId)))
            .andExpect(status().isCreated())
            .andReturn();

    RestResponse response = parseResponse(result, RestResponse.class);
    int id = Integer.parseInt(response.getResult());
    assertThat(id).isGreaterThanOrEqualTo(1);

    Waybill waybill = waybillRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Waybill with id %d not found".formatted(id)));

    assertEquals(type, waybill.getType());
    assertEquals(Double.valueOf(price), waybill.getPrice());
    assertEquals(date, waybill.getDate().toString());
    assertEquals(customerId, waybill.getCustomer().getId());

    mvc.perform(get(WAYBILLS + "/%d".formatted(id))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  }

  @Test
  void testInsert5ValidAnd2InvalidWaybills() throws Exception {
    int customerId = addCustomer();

    for (int i = 0; i < 5; i++) {
      mvc.perform(post(WAYBILL_CREATE)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content("""
                              {
                                  "type": "air-drop",
                                  "price": 70.99,
                                  "date": "2022-07-07",
                                  "customerId": %d
                              }
                              """.formatted(customerId)))
              .andExpect(status().isCreated());
    }

    for (int i = 0; i < 2; i++) {
      mvc.perform(post(WAYBILL_CREATE)
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

    List<Waybill> waybills = waybillRepository.findAll();
    assertEquals(5, waybills.size());
  }

  @Test
  void testAddAndDelete() throws Exception {
    int customerId = addCustomer();

    MvcResult result = mvc.perform(post(WAYBILL_CREATE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                              {
                                  "type": "air-drop",
                                  "price": 70.99,
                                  "date": "2022-07-07",
                                  "customerId": %d
                              }
                              """.formatted(customerId)))
            .andExpect(status().isCreated())
            .andReturn();

    RestResponse response = parseResponse(result, RestResponse.class);
    int id = Integer.parseInt(response.getResult());

    mvc.perform(delete(WAYBILLS + "/%d".formatted(id))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  }

  @Test
  void testUpdate() throws Exception {
    int customerId = addCustomer();

    MvcResult result = mvc.perform(post(WAYBILL_CREATE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                              {
                                  "type": "air-drop",
                                  "price": 70.99,
                                  "date": "2022-07-07",
                                  "customerId": %d
                              }
                              """.formatted(customerId)))
            .andExpect(status().isCreated())
            .andReturn();

    RestResponse response = parseResponse(result, RestResponse.class);
    int id = Integer.parseInt(response.getResult());

    mvc.perform(
            get(WAYBILLS + "/%d".formatted(id))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());

    Waybill waybill = waybillRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Waybill with id %d not found".formatted(id)));

    String newPrice = "1.99";
    String newType = "sea-shipping";

    mvc.perform(patch(WAYBILLS + "/%d".formatted(id))
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {
                      "type": "%s",
                      "price": %s,
                      "date": "%s",
                      "customerId": %d
                    }
                    """.formatted(
                            newType,
                            newPrice,
                            waybill.getDate().toString(),
                            waybill.getCustomer().getId()
                    )
            )).andExpect(status().isOk());

    waybill = waybillRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Waybill with id %d not found".formatted(id)));

    assertEquals(newType, waybill.getType());
    assertEquals(Double.valueOf(newPrice), waybill.getPrice());
  }

  private <T>T parseResponse(MvcResult mvcResult, Class<T> c) {
    try {
      return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), c);
    } catch (JsonProcessingException | UnsupportedEncodingException e) {
      throw new RuntimeException("Error parsing json", e);
    }
  }
}
