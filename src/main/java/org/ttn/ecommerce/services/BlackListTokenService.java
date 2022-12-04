package org.ttn.ecommerce.services;

import org.springframework.http.ResponseEntity;
import org.ttn.ecommerce.entity.user.AccessToken;

public interface BlackListTokenService {
    ResponseEntity<String> blackListToken(String token);

    ResponseEntity<String> logOutUser(String email);

    void addTokenToBlackList(AccessToken accessToken);
}
