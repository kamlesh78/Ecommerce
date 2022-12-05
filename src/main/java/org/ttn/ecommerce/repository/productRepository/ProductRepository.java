package org.ttn.ecommerce.repository.productRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.ttn.ecommerce.entity.product.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query(value = "select count(category_id) from product", nativeQuery = true)
    long existsByCategoryId(@Param("id") Long id);

    @Query(value = "select * from product where seller_user_id = :id",nativeQuery = true)
    Optional<List<Product>> findProductsById(@Param("id") Long id);

    @Modifying
    @Query(value = "update product set is_deleted = 1 where id = :id",nativeQuery = true)
    void deleteById(@Param("id") Long id);
}
