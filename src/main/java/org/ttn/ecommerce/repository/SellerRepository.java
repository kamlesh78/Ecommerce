package org.ttn.ecommerce.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ttn.ecommerce.entity.Seller;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller,Long> {
    Optional<Seller> findByEmail(String email);
    boolean existsByEmail(String email);
    @Modifying
    @Query(value = "UPDATE user SET password = :password WHERE id = :id",nativeQuery = true)
    void updatePassword(@Param("password") String password,@Param("id") Long id);
    @Modifying
    @Query(value = "UPDATE user SET is_active = 0 where id = :id",nativeQuery = true)
    void disableSeller(@Param("id") Long id);



}
