package org.ttn.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.repository.UserRepository;

import java.util.Optional;

@Service
public class UserPasswordService {

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<String> forgetPassword(String email){
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);

    }


}
