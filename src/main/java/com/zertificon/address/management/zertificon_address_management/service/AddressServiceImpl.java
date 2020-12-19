package com.zertificon.address.management.zertificon_address_management.service;

import com.zertificon.address.management.zertificon_address_management.entity.AddressEntity;
import com.zertificon.address.management.zertificon_address_management.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl extends AbstractCrudServiceImpl<AddressEntity> implements AddressService{

    private final AddressRepository repository;

    public AddressServiceImpl(AddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AddressEntity> searchByName(String name) {
        return this.repository.findAddressEntityByName(name);
    }

    @Override
    public Optional<AddressEntity> searchByEmail(String email) {
        return this.repository.findAddressEntityByEmail(email);
    }
}
