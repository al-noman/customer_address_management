package com.zertificon.address.management.zertificon_address_management;

import com.zertificon.address.management.zertificon_address_management.entity.AddressEntity;
import com.zertificon.address.management.zertificon_address_management.repository.AddressRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RepositoryUnitTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private AddressRepository repository;

    @Test
    @DisplayName("search by name should return entity list of entity matched either with firstName or lastName")
    public void searchByNameShouldReturnMatchedNameList(){
        AddressEntity entity1 = constructDummyAddressEntity("a", "b", "a@b.com");
        AddressEntity entity2 = constructDummyAddressEntity("b", "c", "b@c.com");
        entityManager.persist(entity1);
        entityManager.persist(entity2);
        entityManager.flush();

        List<AddressEntity> addressEntities = repository.findAddressEntityByName("b");
        assertThat(addressEntities.size()).isEqualTo(2);
        addressEntities.forEach(address -> {
            assertThat(address.getFirstName()+address.getLastName()).contains("b");
        });

        addressEntities = repository.findAddressEntityByName("a");
        assertThat(addressEntities.size()).isEqualTo(1);
        assertThat(addressEntities.get(0).getFirstName()+addressEntities.get(0).getLastName()).contains("a");
        addressEntities = repository.findAddressEntityByName("c");
        assertThat(addressEntities.size()).isEqualTo(1);
        assertThat(addressEntities.get(0).getFirstName()+addressEntities.get(0).getLastName()).contains("c");

        addressEntities = repository.findAddressEntityByName("x");
        assertThat(addressEntities.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("search by email should return only a single address entity")
    public void searchByEmailShouldReturnSingleAddress(){
        AddressEntity entity1 = constructDummyAddressEntity("a", "b", "a@b.com");
        AddressEntity entity2 = constructDummyAddressEntity("b", "c", "b@c.com");
        entityManager.persist(entity1);
        entityManager.persist(entity2);
        entityManager.flush();

        Optional<AddressEntity> addressEntity = repository.findAddressEntityByEmail("a@b.com");
        assertThat(addressEntity.isPresent()).isTrue();
        assertThat(addressEntity.get().getEmail()).isEqualTo("a@b.com");

        addressEntity = repository.findAddressEntityByEmail("b@c.com");
        assertThat(addressEntity.isPresent()).isTrue();
        assertThat(addressEntity.get().getEmail()).isEqualTo("b@c.com");

        addressEntity = repository.findAddressEntityByEmail("x@x.com");
        assertThat(addressEntity.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("creating address entity should work")
    public void creatingEntityShouldWork(){
        AddressEntity entity1 = constructDummyAddressEntity("a", "b", "a@b.com");
        AddressEntity entity2 = constructDummyAddressEntity("b", "c", "b@c.com");
        entityManager.persist(entity1);
        entityManager.persist(entity2);
        entityManager.flush();

        List<AddressEntity> entities = repository.findAll();
        assertThat(entities.size()).isEqualTo(2);
        entities.forEach(addressEntity -> {
            assertThat(addressEntity.getId()).isNotNull();
            assertThat(addressEntity.getFirstName()+addressEntity.getLastName()).contains("b");
        });
    }

    @Test
    @DisplayName("updating address entity should work")
    public void updatingEntityShouldWork(){
        AddressEntity entity1 = constructDummyAddressEntity("a", "b", "a@b.com");
        AddressEntity persisted1 = entityManager.persist(entity1);
        entityManager.flush();

        persisted1.setFirstName("updatedFirstName");
        persisted1.setLastName("updatedLastName");
        entityManager.persist(persisted1);
        entityManager.flush();

        AddressEntity updatedEntity = entityManager.find(AddressEntity.class, persisted1.getId());
        entityManager.flush();
        assertThat(updatedEntity.getFirstName()).isEqualTo("updatedFirstName");
        assertThat(updatedEntity.getLastName()).isEqualTo("updatedLastName");
    }

    @Test
    @DisplayName("deleting address entity should work")
    public void deletingEntityShouldWork(){
        AddressEntity entity1 = constructDummyAddressEntity("a", "b", "a@b.com");
        AddressEntity persisted1 = entityManager.persist(entity1);
        entityManager.flush();

        AddressEntity returnedEntity = entityManager.find(AddressEntity.class, persisted1.getId());
        entityManager.flush();
        assertThat(returnedEntity).isNotNull();
        assertThat(returnedEntity.getId().toString()).isEqualTo(persisted1.getId().toString());

        entityManager.remove(returnedEntity);
        entityManager.flush();

        AddressEntity deletedEntity = entityManager.find(AddressEntity.class, persisted1.getId());
        entityManager.flush();
        assertThat(deletedEntity).isNull();
    }

    @Test
    @DisplayName("should throw unique email constraint violation exception")
    public void checkUniqueEmailConstraint(){
        try {
            AddressEntity entity1 = constructDummyAddressEntity("a", "b", "a@b.com");
            AddressEntity entity2 = constructDummyAddressEntity("b", "c", "b@c.com");
            AddressEntity entity3 = constructDummyAddressEntity("b", "c", "b@c.com");
            entityManager.persist(entity1);
            entityManager.persist(entity2);
            entityManager.persist(entity3);
            entityManager.flush();
        }catch (PersistenceException exception){
            assertThat(exception.getMessage()).contains("ConstraintViolationException");
        }
    }

    private AddressEntity constructDummyAddressEntity(String firstName, String lastName, String email){
        AddressEntity entity = new AddressEntity();
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setEmail(email);
        return entity;
    }
}
