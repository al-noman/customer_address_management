package com.zertificon.address.management.zertificon_address_management.repository;

import com.zertificon.address.management.zertificon_address_management.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<AddressEntity, UUID> {

    @Query("FROM #{#entityName} address WHERE address.firstName = ?1 OR address.lastName = ?1")
    List<AddressEntity> findAddressEntityByName(String name);
    Optional<AddressEntity> findAddressEntityByEmail(String email);
}
