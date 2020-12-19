package com.zertificon.address.management.zertificon_address_management.service;

import com.zertificon.address.management.zertificon_address_management.entity.AddressEntity;

import java.util.List;
import java.util.Optional;

public interface AddressService extends CrudService<AddressEntity>{

    List<AddressEntity> searchByName(String name);

    Optional<AddressEntity> searchByEmail(String email);
}
