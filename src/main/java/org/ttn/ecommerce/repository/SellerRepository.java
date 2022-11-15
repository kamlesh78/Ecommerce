package org.ttn.ecommerce.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.ttn.ecommerce.entities.Customer;
import org.ttn.ecommerce.entities.Seller;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller,Long> {
    Optional<Seller> findByEmail(String email);

    boolean existsByEmail(String email);
}
