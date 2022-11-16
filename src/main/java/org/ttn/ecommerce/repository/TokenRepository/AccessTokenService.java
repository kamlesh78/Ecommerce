package org.ttn.ecommerce.repository.TokenRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.ttn.ecommerce.entities.Token;

@Repository
public interface AccessTokenService extends JpaRepository<Token,Long> {
}
