package org.ttn.ecommerce.services;

import org.springframework.http.ResponseEntity;
import org.ttn.ecommerce.dto.LoginDto;
import org.ttn.ecommerce.dto.register.CustomerRegisterDto;
import org.ttn.ecommerce.dto.register.SellerRegisterDto;
import org.ttn.ecommerce.entity.UserEntity;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    String emailFromToken(HttpServletRequest request);

    ResponseEntity<String> registerCustomer(CustomerRegisterDto registerDto);

    ResponseEntity<String> registerSeller(SellerRegisterDto sellerRegisterDto);

    ResponseEntity<?> login(LoginDto loginDto, UserEntity user);

    ResponseEntity<String> confirmAccount(UserEntity userEntity, String token);
}
