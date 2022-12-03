package org.ttn.ecommerce.repository.TokenRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ttn.ecommerce.entity.token.BlackListedToken;

import java.util.List;

@Repository
public interface BlackListTokenRepository extends JpaRepository<BlackListedToken,Long> {

    List<BlackListedToken> findByToken(String token);
    boolean existsByToken(String token);
}
