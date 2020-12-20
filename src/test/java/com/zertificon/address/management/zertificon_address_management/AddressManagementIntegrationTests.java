package com.zertificon.address.management.zertificon_address_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zertificon.address.management.zertificon_address_management.model.AddressDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddressManagementIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Address Post should succeed with minimum number of properties defined")
    @Order(1)
    void postShouldSucceedWithMinimumAddressProperties() throws Exception {
        AddressDTO addressDTO = new AddressDTO("Abdullah", "Noman", "alnoman@gmail.com");
        String jsonContent = objectMapper.writeValueAsString(addressDTO);
        mockMvc.perform(post("/addresses")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    AddressDTO returnedAddress = objectMapper.readValue(response.getContentAsString(), AddressDTO.class);
                    assertionOfAddressProperties(returnedAddress);
                });
    }

    @Test
    @DisplayName("Search By Email Should Return Single Dto")
    @Order(2)
    void getByEmailShouldReturnSingleDto() throws Exception {
        mockMvc.perform(get("/addresses/by-email/alnoman@gmail.com")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    AddressDTO returnedAddress = objectMapper.readValue(response.getContentAsString(), AddressDTO.class);
                    assertionOfAddressProperties(returnedAddress);
                });
    }

    @Test
    @DisplayName("Search By Name Should Work For Both FirstName And LastName")
    @Order(3)
    void getByNameShouldWorkForBothByFirstNameAndLastName() throws Exception {
        mockMvc.perform(get("/addresses/by-name/Abdullah")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    AddressDTO[] returnedAddress = objectMapper.readValue(response.getContentAsString(), AddressDTO[].class);
                    Arrays.stream(returnedAddress).forEach(address -> {
                        assertionOfAddressProperties(address);
                    });
                });

        mockMvc.perform(get("/addresses/by-name/Noman")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    AddressDTO[] returnedAddress = objectMapper.readValue(response.getContentAsString(), AddressDTO[].class);
                    Arrays.stream(returnedAddress).forEach(address -> {
                        assertionOfAddressProperties(address);
                    });
                });
    }

    @Test
    @DisplayName("Address PUT should succeed")
    @Order(4)
    void updateShouldSucceed() throws Exception {
        AddressDTO addressDTO = new AddressDTO("Abdullah", "Noman", "alnoman2@gmail.com");
        String jsonContent = objectMapper.writeValueAsString(addressDTO);
        MvcResult mvcResult = mockMvc.perform(post("/addresses").content(jsonContent).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        addressDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AddressDTO.class);

        addressDTO.setFirstName("AbdullahUpdated");
        addressDTO.setLastName("NomanUpdated");
        addressDTO.setDateOfBirth(new Date());
        addressDTO.setZipCode(96052);
        addressDTO.setStreet("Pestalozistrasse");
        addressDTO.setCountry("Germany");
        jsonContent = objectMapper.writeValueAsString(addressDTO);

        String putUri = String.format("/addresses/%s", addressDTO.getId());
        mockMvc.perform(put(putUri)
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    AddressDTO returnedAddress = objectMapper.readValue(response.getContentAsString(), AddressDTO.class);
                    assertThat(returnedAddress.getId()).isNotNull();
                    assertThat(returnedAddress.getVersion()).isEqualTo(1);
                    assertThat(returnedAddress.getFirstName()).isEqualTo("AbdullahUpdated");
                    assertThat(returnedAddress.getLastName()).isEqualTo("NomanUpdated");
                    assertThat(returnedAddress.getEmail()).isEqualTo("alnoman2@gmail.com");
                    assertThat(returnedAddress.getCountry()).isEqualTo("Germany");
                    assertThat(returnedAddress.getStreet()).isEqualTo("Pestalozistrasse");
                    assertThat(returnedAddress.getZipCode()).isEqualTo(96052);
                    assertThat(returnedAddress.getDateOfBirth()).isBeforeOrEqualTo(new Date());
                });
    }

    @Test
    @DisplayName("Delete by id should succeed")
    @Order(5)
    void deleteShouldSucceed() throws Exception {
        AddressDTO addressDTO = new AddressDTO("Abdullah", "Noman", "alnoman3@gmail.com");
        String jsonContent = objectMapper.writeValueAsString(addressDTO);
        MvcResult mvcResult = mockMvc.perform(post("/addresses")
                .content(jsonContent).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        addressDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AddressDTO.class);
        String requestUri = String.format("/addresses/%s", addressDTO.getId());

        mockMvc.perform(get(requestUri)).andExpect(status().isOk());

        mockMvc.perform(delete(requestUri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(requestUri)).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete all should delete all entities")
    @Order(6)
    void deleteAllShouldDeleteAllEntities() throws Exception {
        String requestUri = String.format("/addresses/by-name/%s", "Abdullah");

        mockMvc.perform(get(requestUri))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    AddressDTO[] returnedAddress = objectMapper.readValue(response.getContentAsString(), AddressDTO[].class);
                    assertThat(returnedAddress.length).isGreaterThan(0);
                });

        mockMvc.perform(delete("/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(requestUri))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    AddressDTO[] returnedAddress = objectMapper.readValue(response.getContentAsString(), AddressDTO[].class);
                    assertThat(returnedAddress.length).isEqualTo(0);
                });
    }

    private void assertionOfAddressProperties(AddressDTO returnedAddress) {
        assertThat(returnedAddress.getId()).isNotNull();
        assertThat(returnedAddress.getVersion()).isEqualTo(0);
        assertThat(returnedAddress.getFirstName()).isEqualTo("Abdullah");
        assertThat(returnedAddress.getLastName()).isEqualTo("Noman");
        assertThat(returnedAddress.getEmail()).isEqualTo("alnoman@gmail.com");
        assertThat(returnedAddress.getCountry()).isNull();
        assertThat(returnedAddress.getStreet()).isNull();
        assertThat(returnedAddress.getZipCode()).isEqualTo(0);
        assertThat(returnedAddress.getDateOfBirth()).isNull();
    }
}
