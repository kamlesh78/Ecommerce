package org.ttn.ecommerce.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.ttn.ecommerce.dto.AuthResponseDto;
import org.ttn.ecommerce.dto.LoginDto;
import org.ttn.ecommerce.dto.register.CustomerRegisterDto;
import org.ttn.ecommerce.dto.register.SellerRegisterDto;
import org.ttn.ecommerce.entities.Role;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.repository.RoleRepository;
import org.ttn.ecommerce.repository.TokenRepository.RegisterUserRepository;
import org.ttn.ecommerce.repository.UserRepository;
import org.ttn.ecommerce.security.JWTGenerator;
import org.ttn.ecommerce.services.TokenService;
import org.ttn.ecommerce.services.UserDaoService;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/auth")
public class PublicController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncode;
    private JWTGenerator jwtGenerator;
    private UserDaoService userDaoService;


    public PublicController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncode, JWTGenerator jwtGenerator,UserDaoService userDaoService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncode = passwordEncode;
        this.jwtGenerator = jwtGenerator;
        this.userDaoService = userDaoService;
    }


    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto ){

        UserEntity user = userRepository.findByEmail(loginDto.getEmail()).get();

        if(user.isActive()){
            return new ResponseEntity<>("Account is not active ! Please contact admin to activate it", HttpStatus.BAD_REQUEST);
        }
        return userDaoService.loginCustomer(loginDto,user);
    }

    @GetMapping("hello")
    public String display(){
        return "hello";
    }


    @PostMapping("customer/register")
    public ResponseEntity<String> registerCustomer(@RequestBody CustomerRegisterDto customerRegisterDto){

        return userDaoService.registerCustomer(customerRegisterDto);

    }

    @PostMapping("seller/register")
    public ResponseEntity<String> registerSeller(@RequestBody SellerRegisterDto sellerRegisterDto){

        return userDaoService.registerSeller(sellerRegisterDto);
    }

//    @GetMapping("confirm_account/{email}/{token}")
//    public ResponseEntity<String> confirmAccount(@PathVariable("email") String email,@PathVariable("token") String token){
//
//        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
//        if(userEntity.isPresent()){
//            userDaoService.confirmAccount(userEntity.get(),token);
//        }
//
//    }


    @GetMapping("forget-password")
    public ResponseEntity<?> forgetUserPassword(@PathVariable String Email){
        return
    }
}
