package org.ttn.ecommerce.repository.UserRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.ttn.ecommerce.entity.user.UserEntity;

import java.util.Optional;


@Repository
public interface UserRepository  extends JpaRepository<UserEntity,Long> {
   Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);

    @Modifying
    @Query(value = "UPDATE user SET is_active = 1 where id = :userId",nativeQuery = true)
    void activateUserById(@Param("userId") long id);

    @Modifying
    @Query(value = "UPDATE user SET is_active = 0 where id = :userId",nativeQuery = true)
    void deactivateUserById(@Param("userId") long id);

    @Modifying
    @Query(value = "UPDATE user SET a.invalid_attempt_count = ?1 WHERE a.email = ?2",nativeQuery = true)
    void updateInvalidAttemptCount(Integer invalidAttemptCount, String email);
    Optional<UserEntity> findById(long id);

    @Modifying
    @Query(value = "update user set invalid_attempt_count = :invalidAttemptCount where id = :id",nativeQuery = true)
    void saveInvalidCount(@Param("invalidAttemptCount") int invalidAttemptCount,@Param("id") Long id);

    @Modifying
    @Query(value = "update user set is_locked = true where id = :id",nativeQuery = true)
    void lockAccount(@Param("id") Long id);
}
