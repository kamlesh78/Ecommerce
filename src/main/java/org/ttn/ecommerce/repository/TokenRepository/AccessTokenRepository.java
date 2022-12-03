package org.ttn.ecommerce.repository.TokenRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.ttn.ecommerce.entity.Token;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccessTokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    @Query(value = "select * from access_token where user_id = :id",nativeQuery = true)
    List<Token> findTokensByUserId(@Param("id") Long id);


    @Modifying
    @Query(value = "delete from access_token where user_id = :id",nativeQuery = true)
    void deleteByUserId(@Param("id") Long id);
}
