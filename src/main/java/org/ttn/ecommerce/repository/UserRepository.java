package org.ttn.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.ttn.ecommerce.entities.UserEntity;

import java.util.Optional;


@Repository
public interface UserRepository  extends JpaRepository<UserEntity,Long> {
   Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query(value = "UPDATE user SET is_active = 1 where id = :userId")
    void activateUserById(@Param("userId") Long id);
}
