package com.zertificon.address.management.zertificon_address_management;

import com.zertificon.address.management.zertificon_address_management.api.AddressController;
import com.zertificon.address.management.zertificon_address_management.api.AddressConverter;
import com.zertificon.address.management.zertificon_address_management.entity.AddressEntity;
import com.zertificon.address.management.zertificon_address_management.exception.EntityNotFoundException;
import com.zertificon.address.management.zertificon_address_management.model.AddressDTO;
import com.zertificon.address.management.zertificon_address_management.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class ControllerUnitTest {

    @InjectMocks
    private AddressController controller;
    @Mock
    private AddressConverter converter;
    @Mock
    private AddressService service;

    @BeforeEach
    void init_mocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("search by name should return any entity that match with either firstName or lastName")
    public void searchByNameShouldReturnMatchedItems() throws Exception {
        AddressEntity entity1 = constructDummyAddressEntity("a", "b", "a@b.com");
        AddressEntity entity2 = constructDummyAddressEntity("b", "c", "b@c.com");
        given(service.searchByName("b")).willReturn(Arrays.asList(entity1, entity2));
        given(converter.convertEntityToDto(entity1)).willReturn(
                new AddressDTO(entity1.getFirstName(), entity1.getLastName(), entity1.getEmail()));
        given(converter.convertEntityToDto(entity2)).willReturn(
                new AddressDTO(entity2.getFirstName(), entity2.getLastName(), entity2.getEmail()));

        assertThat(controller.searchByName("b").size()).isEqualTo(2);
        controller.searchByName("b").forEach(addressDTO -> {
            assertThat(addressDTO.getFirstName()+addressDTO.getLastName()).contains("b");
        });
    }

    @Test
    @DisplayName("search by email should return entity not found exception")
    public void searchByEmailShouldReturnNotFoundException() throws Exception {
        given(service.searchByEmail(anyString())).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, ()-> controller.searchByEmail("b@x"));
    }

    @Test
    @DisplayName("search by email should return only one matched dto")
    public void searchByEmailShouldReturnOneDto() throws Exception {
        AddressEntity entity1 = constructDummyAddressEntity("a", "b", "a@b.com");
        AddressEntity entity2 = constructDummyAddressEntity("b", "c", "b@c.com");
        given(service.searchByEmail("b@c.com")).willReturn(Optional.of(entity2));
        given(converter.convertEntityToDto(entity2)).willReturn(
                new AddressDTO(entity2.getFirstName(), entity2.getLastName(), entity2.getEmail()));

        assertThat(controller.searchByEmail("b@c.com").getEmail()).isEqualTo("b@c.com");
        assertThat(controller.searchByEmail("b@c.com").getFirstName()).isEqualTo("b");
        assertThat(controller.searchByEmail("b@c.com").getLastName()).isEqualTo("c");
    }

    private AddressEntity constructDummyAddressEntity(String firstName, String lastName, String email){
        AddressEntity entity = new AddressEntity();
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setEmail(email);
        return entity;
    }
}
