package org.ttn.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ttn.ecommerce.entities.Test;

import java.util.Optional;

public interface TestRepository extends JpaRepository<Test,Long> {

    Optional<Test> findById(Long id);

}
