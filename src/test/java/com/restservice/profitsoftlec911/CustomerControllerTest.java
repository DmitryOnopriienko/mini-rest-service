package com.restservice.profitsoftlec911;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restservice.profitsoftlec911.dto.RestResponse;
import com.restservice.profitsoftlec911.entities.Customer;
import com.restservice.profitsoftlec911.repositories.CustomerRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private ObjectMapper objectMapper;

  private final String CUSTOMERS_CREATE_URI = "/customers/create";

  private final String CUSTOMERS_URI = "/customers";

  @AfterEach
  void clearRepository() {
    customerRepository.deleteAll();
  }

  @Test
  void testCreateNotValidCustomer() throws Exception {
    mvc.perform(post(CUSTOMERS_CREATE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {
                      "name": "NoSurname"
                    }
                    """)
    ).andExpect(status().isBadRequest());
  }

  @Test
  void testCreatValidCustomer() throws Exception {
    String name = "Dmitry";
    String surname = "Onopriienko";
    String patronymic = "Olehovich";
    String json = """
            {
                "name": "%s",
                "surname": "%s",
                "patronymic": "%s"
            }
            """.formatted(name, surname, patronymic);
    MvcResult result = mvc.perform(post(CUSTOMERS_CREATE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated())
            .andReturn();

    RestResponse response = parseResponse(result, RestResponse.class);
    int customerId = Integer.parseInt(response.getResult());
    assertThat(customerId).isGreaterThanOrEqualTo(1);

    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer with id %d not found"
                    .formatted(customerId)));
    assertEquals(name, customer.getName());
    assertEquals(surname, customer.getSurname());
    assertEquals(patronymic, customer.getPatronymic());
  }

  @Test
  void testGetAll_isOk() throws Exception {
    mvc.perform(get(CUSTOMERS_URI).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  }

  @Test
  void testAddOneAndGetById() throws Exception {
    String name = "Dmitry";
    String surname = "Onopriienko";
    String patronymic = "Olehovich";
    String json = """
            {
                "name": "%s",
                "surname": "%s",
                "patronymic": "%s"
            }
            """.formatted(name, surname, patronymic);
    MvcResult result = mvc.perform(post(CUSTOMERS_CREATE_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andExpect(status().isCreated())
            .andReturn();
    int id = Integer.parseInt(parseResponse(result, RestResponse.class).getResult());

    mvc.perform(get(CUSTOMERS_URI + "/%d".formatted(id))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  }

  @Test
  void testAdd3ValidAnd1Invalid() throws Exception {
    String name = "Dmitry";
    String surname = "Onopriienko";
    String patronymic = "Olehovich";
    String json = """
            {
                "name": "%s",
                "surname": "%s",
                "patronymic": "%s"
            }
            """.formatted(name, surname, patronymic);
    String invalidJson = """
            {
              "name": "Nick"
            }
            """;
    for (int i = 0; i < 3; i++) {
      mvc.perform(post(CUSTOMERS_CREATE_URI)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
              .andExpect(status().isCreated())
              .andReturn();
    }
    mvc.perform(post(CUSTOMERS_CREATE_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidJson))
            .andExpect(status().isBadRequest())
            .andReturn();

    List<Customer> customers = customerRepository.findAll();
    assertEquals(3, customers.size());
  }

  private <T>T parseResponse(MvcResult mvcResult, Class<T> c) {
    try {
      return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), c);
    } catch (JsonProcessingException | UnsupportedEncodingException e) {
      throw new RuntimeException("Error parsing json", e);
    }
  }
}
