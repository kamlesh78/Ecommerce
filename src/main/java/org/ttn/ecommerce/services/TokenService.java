package org.ttn.ecommerce.services;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.ttn.ecommerce.entity.user.Customer;
import org.ttn.ecommerce.entity.user.UserEntity;
import org.ttn.ecommerce.entity.token.RefreshToken;

import javax.servlet.http.HttpServletRequest;

public interface TokenService {
    String getUsernameFromJWT(String token);

    String generateRegisterToken(Customer customer);

    RefreshToken generateRefreshToken(UserEntity userEntity);

    @Transactional
    void countAndDeleteRefreshToken(long user_id);

    @Transactional
    String confirmAccount(Long id, String token);

    String getJWTFromRequest(HttpServletRequest request);

    ResponseEntity<?> newAccessToken(String refreshToken);
}
