package org.ttn.ecommerce.repository.TokenRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ttn.ecommerce.entities.token.ActivateUserToken;

public interface RegisterUserRepository extends JpaRepository<ActivateUserToken,Long> {

}
