package com.zertificon.address.management.zertificon_address_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public abstract class AbstractCrudServiceImpl<ENTITY> implements CrudService<ENTITY>{

    @Autowired
    protected JpaRepository<ENTITY, UUID> repository;

    @Override
    public ENTITY addOne(ENTITY entity) {
        return this.repository.save(entity);
    }

    @Override
    public Optional<ENTITY> getOne(UUID id) {
        return this.repository.findById(id);
    }

    @Override
    public ENTITY updateOne(ENTITY entity) {
        return this.repository.save(entity);
    }

    @Override
    public void deleteAll() {
        this.repository.deleteAll();
    }

    @Override
    public void deleteOne(UUID id) {
        this.repository.deleteById(id);
    }
}
