package org.ttn.ecommerce.services;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.ttn.ecommerce.entities.Customer;
import org.ttn.ecommerce.entities.RefreshToken;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.entities.token.ActivateUserToken;
import org.ttn.ecommerce.exception.TokenNotFoundException;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.CustomerRepository;
import org.ttn.ecommerce.repository.TokenRepository.RefreshTokenRepository;
import org.ttn.ecommerce.repository.TokenRepository.RegisterUserRepository;
import org.ttn.ecommerce.repository.UserRepository;
import org.ttn.ecommerce.security.SecurityConstants;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class TokenService {

    @Autowired
    RegisterUserRepository registerUserRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmailServicetry emailServicetry;

    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }



    public String generateRegisterToken(Customer customer){
        String registerToken = UUID.randomUUID().toString();
        ActivateUserToken activateUserToken = new ActivateUserToken();
        activateUserToken.setToken(registerToken);
        activateUserToken.setCreatedAt(LocalDateTime.now());
        activateUserToken.setExpireAt(LocalDateTime.now().plusMinutes(SecurityConstants.REGISTER_TOKEN_EXPIRE_MIN));


        /*changed here*/
        activateUserToken.setUserEntity(customer);


        registerUserRepository.save(activateUserToken);
        return registerToken;
    }

    public RefreshToken generateRefreshToken(UserEntity userEntity){

        countAndDeleteRefreshToken(userEntity.getId());
        String refreshToken = UUID.randomUUID().toString();
        RefreshToken refreshTokenObj = new RefreshToken();
        refreshTokenObj.setToken(refreshToken);
        refreshTokenObj.setExpireAt(LocalDateTime.now().plusHours(SecurityConstants.REFRESH_TOKEN_EXPIRE_HOURS));
        refreshTokenObj.setUserEntity(userEntity);
        return refreshTokenObj;
    }

    @Transactional
    public void countAndDeleteRefreshToken(long user_id){

        long count = refreshTokenRepository.findByUserEntity(user_id);
        if(count >=1){
             refreshTokenRepository.deleteByUserId(user_id);
        }
    }

    @Transactional
    public String  confirmAccount(Long id, String token){

        Customer customer = customerRepository.findById(id).orElseThrow(()->new UserNotFoundException("User with given id "+id+"not found"));

        ActivateUserToken activateUserToken = registerUserRepository.findByTokenAndUserEntity(token,id).orElseThrow(
                ()->new TokenNotFoundException("Activation Token not found or invalid ! Please Provide valid token!"));


        if(activateUserToken.getActivatedAt() !=null){

            return "Email Already confirmed";
        }
        LocalDateTime expireAt = activateUserToken.getExpireAt();
        if(expireAt.isBefore(LocalDateTime.now())){

            registerUserRepository.deleteActivateToken(activateUserToken.getToken(),id);
            String activationToken =  generateRegisterToken(customer);
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(customer.getEmail());
            simpleMailMessage.setSubject("Account Activation Token");
            simpleMailMessage.setText(customer.getFirstName() + " Please Use this Activation Code to activate your account within 3 hours");
            emailServicetry.sendEmail(simpleMailMessage);

            return "New Activation Token sent to your email id .Please activate account within 3 hours";

        }

        registerUserRepository.confirmUserBytoken(token,LocalDateTime.now());
        userRepository.activateUserById(id);

        return "Account Activated";






//
//        Optional<ActivateUserToken> activateUserToken = registerUserRepository.findByTokenAndUserEntity(token,id);
//        String errMessage="";
//        if(activateUserToken.isPresent()){
//
//            System.out.println(activateUserToken.get().getToken());
//            if(activateUserToken.get().getActivatedAt() !=null){
//
//                errMessage="Email Already confirmed";
//                return errMessage;
//            }
//            LocalDateTime expireAt = activateUserToken.get().getExpireAt();
//            if(expireAt.isBefore(LocalDateTime.now())){
//                errMessage= "Token Expired";
//
//            }
//
//            registerUserRepository.confirmUserBytoken(token,LocalDateTime.now());
//            userRepository.activateUserById(id);
//
//
//
//
//        }else{
//            return  "token not found";
//        }
    }


    public String getJWTFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7,bearerToken.length());

        }
        return null;
    }

}
