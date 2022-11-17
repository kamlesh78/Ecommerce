package org.ttn.ecommerce.repository.TokenRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ttn.ecommerce.entities.token.ActivateUserToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RegisterUserRepository extends JpaRepository<ActivateUserToken,Long> {

    @Query(value = "select * from activate_user_token where token = :token and user_id = :id",nativeQuery = true)
    Optional<ActivateUserToken> findByTokenAndUserEntity(@Param("token") String token,@Param("id") Long user_id);

    @Query(value = "UPDATE activate_user_token SET confirm_at = :time where token = :token")
    public void confirmUserBytoken(@Param("token") String token, @Param("time") LocalDateTime time);
}
