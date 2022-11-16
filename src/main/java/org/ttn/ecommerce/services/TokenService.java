package org.ttn.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ttn.ecommerce.entities.Customer;
import org.ttn.ecommerce.entities.RefreshToken;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.entities.token.ActivateUserToken;
import org.ttn.ecommerce.repository.TokenRepository.RegisterUserRepository;
import org.ttn.ecommerce.security.SecurityConstants;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenService {

    @Autowired
    RegisterUserRepository registerUserRepository;


    public String generateRegisterToken(Customer customer){
        String registerToken = UUID.randomUUID().toString();
        ActivateUserToken activateUserToken = new ActivateUserToken();
        activateUserToken.setToken(registerToken);
        activateUserToken.setCreatedAt(LocalDateTime.now());
        activateUserToken.setExpireAt(LocalDateTime.now().plusMinutes(SecurityConstants.REGISTER_TOKEN_EXPIRE_MIN));
        activateUserToken.setCustomer(customer);
        registerUserRepository.save(activateUserToken);
        return registerToken;
    }

    public RefreshToken generateRefreshToken(UserEntity userEntity){
        String refreshToken = UUID.randomUUID().toString();
        RefreshToken refreshTokenObj = new RefreshToken();
        refreshTokenObj.setToken(refreshToken);
        refreshTokenObj.setExpireAt(LocalDateTime.now().plusHours(SecurityConstants.REFRESH_TOKEN_EXPIRE_HOURS));
        refreshTokenObj.setUserEntity(userEntity);
        return refreshTokenObj;
    }


}
