package org.ttn.ecommerce.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttn.ecommerce.entity.user.Token;
import org.ttn.ecommerce.entity.user.UserEntity;
import org.ttn.ecommerce.entity.token.BlackListedToken;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.TokenRepository.AccessTokenRepository;
import org.ttn.ecommerce.repository.TokenRepository.BlackListTokenRepository;
import org.ttn.ecommerce.repository.UserRepository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BlackListTokenService implements org.ttn.ecommerce.services.BlackListTokenService {

    @Autowired
    BlackListTokenRepository blackListTokenRepository;

    @Autowired
    AccessTokenRepository accessTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Override
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


    @Override
    public ResponseEntity<String> logOutUser(String email) {

        UserEntity userEntity =userRepository.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));

        List<Token> accessTokens = accessTokenRepository.findTokensByUserId(userEntity.getId());
        if(accessTokens.size()>0){
            accessTokenRepository.deleteByUserId(userEntity.getId());
            return new ResponseEntity<>("User LogOut Successfully",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Token Not Found ",HttpStatus.NOT_FOUND);
        }


    }

    @Override
    public void addTokenToBlackList(Token accessToken){
        BlackListedToken blackListedToken = new BlackListedToken();
        blackListedToken.setToken(accessToken.getToken());
        blackListedToken.setAccessTokenExpireAt(accessToken.getExpiredAt());
        blackListedToken.setUserId(accessToken.getUserEntity().getId());
        blackListTokenRepository.save(blackListedToken);
    }
}
