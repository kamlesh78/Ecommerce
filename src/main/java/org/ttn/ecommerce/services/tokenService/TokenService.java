package org.ttn.ecommerce.services.tokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.ttn.ecommerce.dto.AuthResponseDto;
import org.ttn.ecommerce.entity.Customer;
import org.ttn.ecommerce.entity.Role;
import org.ttn.ecommerce.entity.Token;
import org.ttn.ecommerce.entity.token.RefreshToken;
import org.ttn.ecommerce.entity.UserEntity;
import org.ttn.ecommerce.entity.token.ActivateUserToken;
import org.ttn.ecommerce.exception.TokenNotFoundException;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.CustomerRepository;
import org.ttn.ecommerce.repository.TokenRepository.AccessTokenRepository;
import org.ttn.ecommerce.repository.TokenRepository.RefreshTokenRepository;
import org.ttn.ecommerce.repository.TokenRepository.RegisterUserRepository;
import org.ttn.ecommerce.repository.UserRepository;
import org.ttn.ecommerce.security.SecurityConstants;
import org.ttn.ecommerce.services.EmailServicetry;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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

    @Autowired
    AccessTokenRepository accessTokenRepository;

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

        Customer customer = customerRepository.findById(id).
                orElseThrow(()->new UserNotFoundException("User with given id "+id+"not found"));

        ActivateUserToken activateUserToken = registerUserRepository.findByTokenAndUserEntity(token,id)
                .orElseThrow(()->new TokenNotFoundException("Activation Token not found or invalid ! Please Provide valid token!"));


        if(activateUserToken.getActivatedAt() !=null){

            return "Email Already confirmed";
        }
        LocalDateTime expireAt = activateUserToken.getExpireAt();
        if(expireAt.isBefore(LocalDateTime.now())){


            /*
                   If Token Expired Resend New Activation Token To The User
             */
            registerUserRepository.deleteActivateToken(activateUserToken.getToken(),id);
            String activationToken =  generateRegisterToken(customer);
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(customer.getEmail());
            simpleMailMessage.setSubject("Account Activation Token");
            simpleMailMessage.setText(customer.getFirstName() + " Please Use this Activation Code to activate your account within 3 hours"
                    +"\nPlease use this Token : " + activationToken);
            emailServicetry.sendEmail(simpleMailMessage);

            return "New Activation Token sent to your email id .Please activate account within 3 hours";

        }

        registerUserRepository.confirmUserBytoken(token,LocalDateTime.now());
        userRepository.activateUserById(id);

        return "Account Activated";
    }


    public String getJWTFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7,bearerToken.length());

        }
        return null;
    }


    /**
     *      Generate New Access Token Using Refresh Token
     */
    public ResponseEntity<?> newAccessToken(String refreshToken) {
        RefreshToken refreshToken1 = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(()->new TokenNotFoundException("Refresh Token not found or invalid ! Please Provide a valid refresh token!"));

        UserEntity userEntity = refreshToken1.getUserEntity();
        LocalDateTime expireAt = refreshToken1.getExpireAt();

        /**      If Refresh Token Expired        */
        if(expireAt.isBefore(LocalDateTime.now())){
            throw new TokenNotFoundException("Refresh Token Got Expired" +"\n"+"Please login Again to generate new Access and Refresh Token");
        }else{


            String username = userEntity.getEmail();
            Date currentDate = new Date();
            Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

                String role = "";

                List<Role> roles  = (List<Role>) userEntity.getRoles();
                for(Role r : roles){
                    if(r.getAuthority().equals("ROLE_SELLER")){
                        role = "SELLER";
                    }
                    if(r.getAuthority().equals("ROLE_CUSTOMER")){
                        role = "CUSTOMER";
                    }
                    if(r.getAuthority().equals("ROLE_ADMIN")){
                        role = "ADMIN";
                    }
                }
            String token = Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .claim("ROLE", role)
                    .setExpiration(expireDate)
                    .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET)
                    .compact();


            /* Access Token */
            Token accessToken = new Token();
            accessToken.setUserEntity(userEntity);
            accessToken.setToken(token);
            accessToken.setCreatedAt(LocalDateTime.now());
            accessToken.setExpiredAt(LocalDateTime.now().plusMinutes(SecurityConstants.JWT_EXPIRATION));
            accessTokenRepository.save(accessToken);


            return new ResponseEntity<>(new AuthResponseDto(accessToken.getToken(),refreshToken), HttpStatus.CREATED);
        }
    }
}
