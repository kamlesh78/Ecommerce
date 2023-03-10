package org.ttn.ecommerce.repository.TokenRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.ttn.ecommerce.entity.token.ActivateUserToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RegisterUserRepository extends JpaRepository<ActivateUserToken,Long> {

    @Query(value = "select * from activate_user_token where token = :token and user_id = :id",nativeQuery = true)
    Optional<ActivateUserToken> findByTokenAndUserEntity(@Param("token") String token,@Param("id") Long user_id);


    @Query(value = "select count(user_id) from activate_user_token where user_id = :id",nativeQuery = true)
    long existsByUserId(@Param("id") Long id);
    @Modifying
    @Query(value="DELETE FROM activate_user_token where token = :token and user_id = :id",nativeQuery = true)
    void deleteActivateToken(@Param("token") String token,@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE activate_user_token SET activated_at = :time where token = :token",nativeQuery = true)
    void confirmUserBytoken(@Param("token") String token, @Param("time") LocalDateTime time);


    @Modifying
    @Query(value = "DELETE FROM activate_user_token where user_id = :id ",nativeQuery = true)
    void deleteByUserId(@Param("id") Long id);

}
