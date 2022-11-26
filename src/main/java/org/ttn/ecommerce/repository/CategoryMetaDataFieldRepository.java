package org.ttn.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ttn.ecommerce.entities.category.CategoryMetaDataField;

import java.util.Optional;

@Repository
public interface CategoryMetaDataFieldRepository extends JpaRepository<CategoryMetaDataField,Long> {

    Optional<CategoryMetaDataField> findByName(String name);

 //   Optional<CategoryMetaDataField> findByCategoryId(Long id);


}
