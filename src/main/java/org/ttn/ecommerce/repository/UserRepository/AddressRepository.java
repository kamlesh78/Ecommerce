package org.ttn.ecommerce.repository.UserRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ttn.ecommerce.entity.user.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,Long> {

    Optional<Address> findById(Long id);


    @Query(value = "select count(user_id) from address where id = :id ",nativeQuery = true)
    long existsByUserId(@Param("id") Long id);

    @Query(value = "select * from address where user_id = :id",nativeQuery = true)
    List<Address> findByUserId(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM address where user_id = :id",nativeQuery = true)
    void deleteByUserId(@Param("id") Long id);

 //   boolean existById(Long id);
    @Modifying
    @Query(value = "delete from address where id = :id",nativeQuery = true)
    void deleteAddressById(@Param("id") Long id);



}
