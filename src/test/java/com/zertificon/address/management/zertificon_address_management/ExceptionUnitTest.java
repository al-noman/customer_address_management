package com.zertificon.address.management.zertificon_address_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zertificon.address.management.zertificon_address_management.api.AddressController;
import com.zertificon.address.management.zertificon_address_management.api.AddressConverter;
import com.zertificon.address.management.zertificon_address_management.exception.ExceptionResponse;
import com.zertificon.address.management.zertificon_address_management.model.AddressDTO;
import com.zertificon.address.management.zertificon_address_management.service.AddressService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
public class ExceptionUnitTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AddressConverter converter;
    @MockBean
    private AddressService service;

    @Test
    @DisplayName("search by email should return entity not found exception")
    public void searchByEmailShouldReturnNotFoundException() throws Exception {
        given(service.searchByEmail("xyz")).willReturn(Optional.empty());

        mockMvc.perform(get("/addresses/by-email/xyz")).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    ExceptionResponse exceptionResponse = objectMapper.
                            readValue(response.getContentAsString(), ExceptionResponse.class);
                    assertThat(exceptionResponse.getMessage()).isEqualTo("xyz");
                    assertThat(exceptionResponse.getDetails()).isEqualTo("uri=/addresses/by-email/xyz");
                });
    }

    @Test
    @DisplayName("update by id should return entity not found exception")
    public void updateByIdShouldReturnNotFoundException() throws Exception {
        UUID uuid = UUID.randomUUID();
        given(service.getOne(uuid)).willReturn(Optional.empty());

        String uri = String.format("/addresses/%s", uuid);
        mockMvc.perform(put(uri).
                content(objectMapper.writeValueAsString(
                        new AddressDTO("hello", "world", "a@b.com"))).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    ExceptionResponse exceptionResponse = objectMapper.
                            readValue(response.getContentAsString(), ExceptionResponse.class);
                    assertThat(exceptionResponse.getMessage()).isEqualTo(uuid.toString());
                    assertThat(exceptionResponse.getDetails()).isEqualTo("uri=/addresses/"+uuid.toString());
                });
    }

    @Test
    @DisplayName("delete by id should return entity not found exception")
    public void deleteByIdShouldReturnNotFoundException() throws Exception {
        UUID uuid = UUID.randomUUID();
        given(service.getOne(uuid)).willReturn(Optional.empty());

        String uri = String.format("/addresses/%s", uuid);
        mockMvc.perform(delete(uri).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    ExceptionResponse exceptionResponse = objectMapper.
                            readValue(response.getContentAsString(), ExceptionResponse.class);
                    assertThat(exceptionResponse.getMessage()).isEqualTo(uuid.toString());
                    assertThat(exceptionResponse.getDetails()).isEqualTo("uri=/addresses/"+uuid.toString());
                });
    }

    @Test
    @DisplayName("post request should return bad request param value exception for empty properties")
    public void badRequestParamValueExceptionTest() throws Exception {

        mockMvc.perform(post("/addresses").
                content(objectMapper.writeValueAsString(new AddressDTO())).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    ExceptionResponse exceptionResponse = objectMapper.
                            readValue(response.getContentAsString(), ExceptionResponse.class);
                    assertThat(exceptionResponse.getMessage()).isEqualTo("Validation failed");
                    assertThat(exceptionResponse.getDetails()).contains("NotEmpty.firstName");
                    assertThat(exceptionResponse.getDetails()).contains("NotEmpty.lastName");
                    assertThat(exceptionResponse.getDetails()).contains("NotEmpty.email");
                });
    }

    @Test
    @DisplayName("post request should return constraint violation exception for invalid email")
    public void shouldReturnConstraintViolationException() throws Exception {
        mockMvc.perform(post("/addresses").
                content(objectMapper.writeValueAsString(new AddressDTO("a", "b", "c"))).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    ExceptionResponse exceptionResponse = objectMapper.
                            readValue(response.getContentAsString(), ExceptionResponse.class);
                    assertThat(exceptionResponse.getMessage()).isEqualTo("Validation failed");
                    assertThat(exceptionResponse.getDetails()).doesNotContain("NotEmpty.firstName");
                    assertThat(exceptionResponse.getDetails()).doesNotContain("NotEmpty.lastName");
                    assertThat(exceptionResponse.getDetails()).contains("addressDTO.email");
                    assertThat(exceptionResponse.getDetails()).contains("must be a well-formed email address");
                });
    }

    @Test
    @DisplayName("converter runtime exception should return internal server error")
    public void shouldReturnRuntimeException() throws Exception {
        given(converter.convertDtoToEntity(any())).willThrow(RuntimeException.class);

        mockMvc.perform(post("/addresses").
                content(objectMapper.writeValueAsString(new AddressDTO("a", "b", "a@b.com"))).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isInternalServerError()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    ExceptionResponse exceptionResponse = objectMapper.
                            readValue(response.getContentAsString(), ExceptionResponse.class);
                    assertThat(exceptionResponse.getDetails()).isEqualTo("uri=/addresses");
                });
    }
}
