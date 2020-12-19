package com.zertificon.address.management.zertificon_address_management.api;

import com.zertificon.address.management.zertificon_address_management.entity.AddressEntity;
import com.zertificon.address.management.zertificon_address_management.model.AddressDTO;
import org.springframework.stereotype.Component;

@Component
public class AddressConverter implements CrudConverter<AddressEntity, AddressDTO>{

    @Override
    public AddressEntity convertDtoToEntity(AddressDTO dto){
        AddressEntity entity = new AddressEntity();
        entity.setId(dto.getId());
        entity.setVersion(dto.getVersion());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setStreet(dto.getStreet());
        entity.setZipCode(dto.getZipCode());
        entity.setCountry(dto.getCountry());
        return entity;
    }

    @Override
    public AddressDTO convertEntityToDto(AddressEntity entity){
        AddressDTO dto = new AddressDTO();
        dto.setId(entity.getId());
        dto.setVersion(entity.getVersion());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setStreet(entity.getStreet());
        dto.setZipCode(entity.getZipCode());
        dto.setCountry(entity.getCountry());
        return dto;
    }
}
