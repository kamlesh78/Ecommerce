package org.ttn.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ttn.ecommerce.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);
    @Modifying
    @Query(value = "UPDATE user SET password = :password WHERE id = :id",nativeQuery = true)
    void updatePassword(@Param("password") String password,@Param("id") Long id);

//    Page<Customer> findAll(Pageable pageable);
    List<Customer> findAll();


}
