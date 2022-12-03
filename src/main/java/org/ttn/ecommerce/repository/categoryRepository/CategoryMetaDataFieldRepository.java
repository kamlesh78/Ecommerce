package org.ttn.ecommerce.repository.categoryRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ttn.ecommerce.entity.category.CategoryMetaDataField;

import java.util.Optional;

@Repository
public interface CategoryMetaDataFieldRepository extends JpaRepository<CategoryMetaDataField, Long> {

    Optional<CategoryMetaDataField> findByName(String name);

    //   Optional<CategoryMetaDataField> findByCategoryId(Long id);


}
