package org.ttn.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ttn.ecommerce.entities.product.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query(value = "select count(category_id) from product", nativeQuery = true)
    long existsByCategoryId(@Param("id") Long id);

}
