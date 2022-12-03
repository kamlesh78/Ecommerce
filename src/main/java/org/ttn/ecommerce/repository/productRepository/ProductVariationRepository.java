package org.ttn.ecommerce.repository.productRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ttn.ecommerce.entity.product.ProductVariation;

@Repository
public interface ProductVariationRepository extends JpaRepository<ProductVariation,Long> {


}
