package org.ttn.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ttn.ecommerce.entities.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,Long> {

    Optional<Address> findById(Long id);

 //   boolean existById(Long id);
    @Modifying
    @Query(value = "delete from address where id = :id",nativeQuery = true)
    void deleteAddressById(@Param("id") Long id);



}
