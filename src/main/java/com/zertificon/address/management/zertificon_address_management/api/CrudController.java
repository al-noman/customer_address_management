package com.zertificon.address.management.zertificon_address_management.api;

import com.zertificon.address.management.zertificon_address_management.entity.AbstractEntity;
import com.zertificon.address.management.zertificon_address_management.exception.EntityNotFoundException;
import com.zertificon.address.management.zertificon_address_management.model.AbstractDTO;
import com.zertificon.address.management.zertificon_address_management.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

public abstract class CrudController<ENTITY extends AbstractEntity, DTO extends AbstractDTO> {

    @Autowired
    private CrudService<ENTITY> service;

    @Autowired
    private CrudConverter<ENTITY, DTO> converter;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DTO addOne(@Valid @RequestBody DTO dto){
        ENTITY entity = this.converter.convertDtoToEntity(dto);
        ENTITY storedEntity = this.service.addOne(entity);
        return this.converter.convertEntityToDto(storedEntity);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DTO updateOne(@PathVariable UUID id, @Valid @RequestBody DTO dto){
        Optional<ENTITY> maybeEntity = this.service.getOne(id);
        ENTITY entity = maybeEntity.orElseThrow(() -> new EntityNotFoundException(id.toString()));
        return this.converter.convertEntityToDto(
                this.service.updateOne(
                        this.converter.convertDtoToEntity(dto)));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll(){
        this.service.deleteAll();
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOne(@PathVariable UUID id){
        Optional<ENTITY> maybeEntity = this.service.getOne(id);
        maybeEntity.orElseThrow(() -> new EntityNotFoundException(id.toString()));
        this.service.deleteOne(id);
    }
}
