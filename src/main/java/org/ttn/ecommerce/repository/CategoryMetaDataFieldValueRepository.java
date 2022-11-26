package org.ttn.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.ttn.ecommerce.entities.category.CategoryMetadataFieldValue;

import java.util.Optional;

@Repository
public interface CategoryMetaDataFieldValueRepository extends JpaRepository<CategoryMetadataFieldValue,Long> {

    Optional<CategoryMetadataFieldValue> findCategoryFieldValueById(Long id);
}
