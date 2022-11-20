package org.ttn.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ttn.ecommerce.entities.Images;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Images,Long> {

    @Query(value = "select * from profile_image where user_id = :id",nativeQuery = true)
    Optional<Images> findByUserId(@Param("id") long id);
}
