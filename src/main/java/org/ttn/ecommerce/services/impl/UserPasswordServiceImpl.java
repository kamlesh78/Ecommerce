package org.ttn.ecommerce.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttn.ecommerce.dto.reset.ResetPasswordDto;
import org.ttn.ecommerce.entity.user.Token;
import org.ttn.ecommerce.entity.user.UserEntity;
import org.ttn.ecommerce.entity.token.ForgetPasswordToken;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.TokenRepository.AccessTokenRepository;
import org.ttn.ecommerce.repository.TokenRepository.ForgetPasswordRepository;
import org.ttn.ecommerce.repository.UserRepository.UserRepository;
import org.ttn.ecommerce.security.SecurityConstants;
import org.ttn.ecommerce.services.BlackListTokenService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserPasswordServiceImpl implements org.ttn.ecommerce.services.UserPasswordService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ForgetPasswordRepository forgetPasswordRepository;

    @Autowired
    EmailServicetry emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccessTokenRepository accessTokenRepository;

    @Autowired
    BlackListTokenService blackListTokenService;

    @Override
    public ResponseEntity<String> forgetPassword(String email){

        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if(userEntity.isPresent()){
            Optional<ForgetPasswordToken> forgetPasswordToken = forgetPasswordRepository.getTokenByUserId(userEntity.get().getId());


            /* Check Expire */

            /* If forget password token already exist*/
                if(forgetPasswordToken.isPresent()){
                    forgetPasswordRepository.deleteByTokenId(forgetPasswordToken.get().getId());
                }
               ForgetPasswordToken forgetPassword = forgerPassGenerate(userEntity.get());
                forgetPasswordRepository.save(forgetPassword);

            /* Send Mail with rest password Link */
            String toMail = userEntity.get().getEmail();
            String subject = "Reset Password";
            String message = "To reset your password use the token within 15 minutes \n"
                    + "URL : http://127.0.0.1:8080/api/public/reset-password/ \n"
                    + "Token : "
                    + forgetPassword.getToken();

            emailService.sendEmail(toMail,subject,message);



                return new ResponseEntity<>("Reset Password Token Generated || Please check your email || Verify within 15 minutes",HttpStatus.OK);
        }else{


            return new ResponseEntity<>("User with email : " + email + "not found" , HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ForgetPasswordToken forgerPassGenerate(UserEntity userEntity){
        String token = UUID.randomUUID().toString();
        ForgetPasswordToken forgetPasswordToken= new ForgetPasswordToken();
        forgetPasswordToken.setToken(token);
        forgetPasswordToken.setUserEntity(userEntity);
        forgetPasswordToken.setCreatedAt(LocalDateTime.now());
        forgetPasswordToken.setExpireAt(LocalDateTime.now());

        return forgetPasswordToken;
    }

    @Override
    public boolean verifyToken(LocalDateTime localDateTime){

        Duration timeDiff = Duration.between(localDateTime,LocalDateTime.now());
        return timeDiff.toMinutes() >= SecurityConstants.RESET_PASS_EXPIRE_MINUTES;

    }


    @Override
    @Transactional
    public ResponseEntity<String> resetUserPassword(ResetPasswordDto resetPasswordDto) {


        UserEntity userEntity = userRepository.findByEmail(resetPasswordDto.getEmail())
                .orElseThrow(()->new UserNotFoundException("User Not Found With Given Email"));
        Optional<ForgetPasswordToken> forgetPasswordToken = forgetPasswordRepository.findByToken(resetPasswordDto.getToken());


        if(forgetPasswordToken.isPresent()){

            /**  Check If Token Belongs to Current User     */

                if(!forgetPasswordToken.get().getUserEntity().getEmail().equals(userEntity.getEmail())){
                    return new ResponseEntity<>("Provided Token Does Not Belong TO Given Email Address\n Please Provide Correct Token",HttpStatus.BAD_REQUEST);
                }

            if(verifyToken(forgetPasswordToken.get().getExpireAt())){
                forgetPasswordRepository.deleteByTokenId(forgetPasswordToken.get().getId());
                return new ResponseEntity<>("Token has been expired" , HttpStatus.BAD_REQUEST);
            }
            else{

                    userEntity.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
                    userRepository.save(userEntity);
                    forgetPasswordRepository.deleteByTokenId(forgetPasswordToken.get().getId());


                    /*
                            * Delete All Access Token Of Your After Password Reset
                            * User Can not log in from old tokens
                     */

                    List<Token> accessTokenList  = accessTokenRepository.findTokensByUserId(userEntity.getId());
                    if(accessTokenList !=null){
                        accessTokenRepository.deleteByUserId(userEntity.getId());
                    }


                /*            Sending Password Reset Alert               */


                String toMail = userEntity.getEmail();
                String subject = "Password Changed";
                String message = userEntity.getFirstName() + " Password for Your account Changed." +
                        "recently \n Please contact admin if that was not you";

                emailService.sendEmail(toMail,subject,message);

                 return new ResponseEntity<>("Password changed",HttpStatus.OK);
            }
        }else{
            return new ResponseEntity<>("Reset Token not found",HttpStatus.BAD_REQUEST);


        }

    }
}
