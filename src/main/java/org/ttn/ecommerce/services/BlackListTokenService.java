package org.ttn.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttn.ecommerce.entities.Token;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.entities.token.BlackListedToken;
import org.ttn.ecommerce.repository.TokenRepository.AccessTokenRepository;
import org.ttn.ecommerce.repository.TokenRepository.BlackListTokenRepository;
import org.ttn.ecommerce.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class BlackListTokenService {

    @Autowired
    BlackListTokenRepository blackListTokenRepository;

    @Autowired
    AccessTokenRepository accessTokenRepository;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<String> blackListToken(String token) {

        /* finding Access token expire time using token value*/
        Optional<Token> accessToken = accessTokenRepository.findByToken(token);

        /*Exception*/

        if(accessToken.isPresent()){
            BlackListedToken blackListedToken = new BlackListedToken();
            blackListedToken.setToken(token);
            blackListedToken.setAccessTokenExpireAt(accessToken.get().getExpiredAt());
            blackListedToken.setUserId(accessToken.get().getUserEntity().getId());
            blackListTokenRepository.save(blackListedToken);

            return new ResponseEntity<>("Logout Successfully", HttpStatus.OK);

        }else{
            return new ResponseEntity<>("Access Token Not Found",HttpStatus.BAD_REQUEST);
            /* Exception*/
        }

    }

    public void addTokenToBlackList(Token accessToken){
        BlackListedToken blackListedToken = new BlackListedToken();
        blackListedToken.setToken(accessToken.getToken());
        blackListedToken.setAccessTokenExpireAt(accessToken.getExpiredAt());
        blackListedToken.setUserId(accessToken.getUserEntity().getId());
        blackListTokenRepository.save(blackListedToken);
    }
}
