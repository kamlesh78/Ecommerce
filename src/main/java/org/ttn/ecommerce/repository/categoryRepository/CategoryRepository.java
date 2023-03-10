package org.ttn.ecommerce.repository.categoryRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.ttn.ecommerce.entity.category.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    List<Category> findByParentCategory(Category category);

    @Query(value = "SELECT * FROM category where parent_category_id IS NULL", nativeQuery = true)
    List<Category> findAllRootCategories();
}
