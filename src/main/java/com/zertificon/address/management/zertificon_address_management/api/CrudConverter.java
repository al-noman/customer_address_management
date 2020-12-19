package com.zertificon.address.management.zertificon_address_management.api;

public interface CrudConverter<ENTITY, DTO> {
    ENTITY convertDtoToEntity(DTO dto);
    DTO convertEntityToDto(ENTITY entity);
}
