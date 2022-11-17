package org.ttn.ecommerce.repository.TokenRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ttn.ecommerce.entities.token.ForgetPasswordToken;

public interface ForgetPasswordRepository extends JpaRepository<ForgetPasswordToken,Long> {

}
