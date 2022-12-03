package org.ttn.ecommerce.services;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.ttn.ecommerce.dto.reset.ResetPasswordDto;
import org.ttn.ecommerce.entity.UserEntity;
import org.ttn.ecommerce.entity.token.ForgetPasswordToken;

import java.time.LocalDateTime;

public interface UserPasswordService {
    ResponseEntity<String> forgetPassword(String email);

    ForgetPasswordToken forgerPassGenerate(UserEntity userEntity);

    boolean verifyToken(LocalDateTime localDateTime);

    @Transactional
    ResponseEntity<String> resetUserPassword(ResetPasswordDto resetPasswordDto);
}
