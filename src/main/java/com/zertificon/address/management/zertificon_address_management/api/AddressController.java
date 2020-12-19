package com.zertificon.address.management.zertificon_address_management.api;

import com.zertificon.address.management.zertificon_address_management.entity.AddressEntity;
import com.zertificon.address.management.zertificon_address_management.exception.EntityNotFoundException;
import com.zertificon.address.management.zertificon_address_management.model.AddressDTO;
import com.zertificon.address.management.zertificon_address_management.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(path = "/addresses")
public class AddressController extends CrudController<AddressEntity, AddressDTO>{

    private final CrudConverter<AddressEntity, AddressDTO> converter;
    private final AddressService service;

    public AddressController(CrudConverter<AddressEntity, AddressDTO> converter, AddressService service) {
        this.converter = converter;
        this.service = service;
    }

    @GetMapping(path = "by-name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<AddressDTO> searchByName(@PathVariable String name) {
        List<AddressEntity> entityList = this.service.searchByName(name);
        return entityList.stream()
                .map(entity -> this.converter.convertEntityToDto(entity))
                .collect(toList());
    }

    @GetMapping(path = "by-email/{email}")
    @ResponseStatus(HttpStatus.OK)
    public AddressDTO searchByEmail(@PathVariable String email) {
        Optional<AddressEntity> maybeEntity = this.service.searchByEmail(email);
        return maybeEntity
                .map(entity -> this.converter.convertEntityToDto(entity))
                .orElseThrow(() -> new EntityNotFoundException(email));
    }
}
