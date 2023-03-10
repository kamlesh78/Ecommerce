package org.ttn.ecommerce.repository.categoryRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ttn.ecommerce.entity.category.CategoryMetadataFieldValue;
import org.ttn.ecommerce.entity.category.MetaDataValueId;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryMetaDataFieldValueRepository extends JpaRepository<CategoryMetadataFieldValue, MetaDataValueId> {

    Optional<CategoryMetadataFieldValue> findCategoryFieldValueById(Long id);

    List<CategoryMetadataFieldValue> findByCategoryId(Long categoryId);
}
