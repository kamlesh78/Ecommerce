package org.ttn.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttn.ecommerce.dto.reset.ResetPasswordDto;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.entities.token.ForgetPasswordToken;
import org.ttn.ecommerce.repository.TokenRepository.ForgetPasswordRepository;
import org.ttn.ecommerce.repository.UserRepository;
import org.ttn.ecommerce.security.SecurityConstants;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserPasswordService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ForgetPasswordRepository forgetPasswordRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

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
                emailService.setSubject("Reset Password");
                emailService.setMessage("To reset you password click the link below within 15 minutes \n"
                + "http://127.0.0.1:8080/api/auth/forget-password/"
                +forgetPassword.getToken());
                emailService.setToEmail(userEntity.get().getEmail());
                emailService.sendEmail();

                return new ResponseEntity<>("Reset Password Token Generated || Please check your email || Verify within 15 minutes",HttpStatus.OK);
        }else{


            return new ResponseEntity<>("User with email : " + email + "not found" , HttpStatus.BAD_REQUEST);
        }
    }


    public ForgetPasswordToken forgerPassGenerate(UserEntity userEntity){
        String token = UUID.randomUUID().toString();
        ForgetPasswordToken forgetPasswordToken= new ForgetPasswordToken();
        forgetPasswordToken.setToken(token);
        forgetPasswordToken.setUserEntity(userEntity);
        forgetPasswordToken.setCreatedAt(LocalDateTime.now());
        forgetPasswordToken.setExpireAt(LocalDateTime.now());

        return forgetPasswordToken;
    }

    public boolean verifyToken(LocalDateTime localDateTime){

        Duration timeDiff = Duration.between(localDateTime,LocalDateTime.now());
        return timeDiff.toMinutes() >= SecurityConstants.RESET_PASS_EXPIRE_MINUTES;

    }


    @Transactional
    public ResponseEntity<String> resetUserPassword(ResetPasswordDto resetPasswordDto) {

       /* Add validation here */
        UserEntity userEntity = userRepository.findByEmail(resetPasswordDto.getEmail()).get();
        Optional<ForgetPasswordToken> forgetPasswordToken = forgetPasswordRepository.findByToken(resetPasswordDto.getToken());

        if(forgetPasswordToken.isPresent()){
            if(verifyToken(forgetPasswordToken.get().getExpireAt())){
                forgetPasswordRepository.deleteByTokenId(forgetPasswordToken.get().getId());
                return new ResponseEntity<>("Token has been expired" , HttpStatus.BAD_REQUEST);
            }
            else{
                System.out.println("reached here");
                    userEntity.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
                    userRepository.save(userEntity);

                    forgetPasswordRepository.deleteByTokenId(forgetPasswordToken.get().getId());

                    /* Send Email here*/

                 return new ResponseEntity<>("Password changed",HttpStatus.OK);
            }
        }else{
            return new ResponseEntity<>("Reset Token not found",HttpStatus.BAD_REQUEST);


        }

    }
}
