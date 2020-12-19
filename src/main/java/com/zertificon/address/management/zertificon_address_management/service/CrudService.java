package com.zertificon.address.management.zertificon_address_management.service;

import java.util.Optional;
import java.util.UUID;

public interface CrudService<ENTITY> {

    ENTITY addOne(ENTITY entity);

    Optional<ENTITY> getOne(UUID id);

    ENTITY updateOne(ENTITY entity);

    void deleteAll();

    void deleteOne(UUID id);
}
