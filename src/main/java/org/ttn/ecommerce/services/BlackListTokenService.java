package org.ttn.ecommerce.services;

import org.springframework.http.ResponseEntity;
import org.ttn.ecommerce.entity.user.Token;

public interface BlackListTokenService {
    ResponseEntity<String> blackListToken(String token);

    ResponseEntity<String> logOutUser(String email);

    void addTokenToBlackList(Token accessToken);
}
