package org.ttn.ecommerce.repository.TokenRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ttn.ecommerce.entities.RefreshToken;

public interface RefreshTokenService extends JpaRepository<RefreshToken,Long> {
}
