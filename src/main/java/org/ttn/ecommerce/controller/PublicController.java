package org.ttn.ecommerce.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.ttn.ecommerce.dto.LoginDto;
import org.ttn.ecommerce.dto.reset.ResetPasswordDto;
import org.ttn.ecommerce.dto.register.CustomerRegisterDto;
import org.ttn.ecommerce.dto.register.SellerRegisterDto;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.RoleRepository;
import org.ttn.ecommerce.repository.UserRepository;
import org.ttn.ecommerce.security.JWTGenerator;
import org.ttn.ecommerce.services.*;
import org.ttn.ecommerce.services.tokenService.BlackListTokenService;
import org.ttn.ecommerce.services.tokenService.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/public")
public class PublicController {


    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncode;
    private final JWTGenerator jwtGenerator;
    private final UserService userService;
    private final UserPasswordServiceImpl userPasswordService;
    private final TokenService tokenService;
    private final BlackListTokenService blackListTokenService;
    private final CustomerServiceImpl customerDaoService;
    private MessageSource messageSource;


    @Autowired
    public PublicController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncode, JWTGenerator jwtGenerator, UserServiceImpl userService, UserPasswordServiceImpl userPasswordService, TokenService tokenService, BlackListTokenService blackListTokenService, CustomerServiceImpl customerDaoService, MessageSource messageSource) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncode = passwordEncode;
        this.jwtGenerator = jwtGenerator;
        this.userService = userService;
        this.userPasswordService = userPasswordService;
        this.tokenService = tokenService;
        this.blackListTokenService = blackListTokenService;
        this.customerDaoService = customerDaoService;
        this.messageSource = messageSource;
    }


    /*Common Login for Customer and Seller*/
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

        UserEntity user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new UserNotFoundException("User with this email not found"));

        if (!user.isActive()) {
            return new ResponseEntity<>("Account is not active ! Please contact admin to activate it", HttpStatus.BAD_REQUEST);
        }

        if (user.isLocked()) {
            return new ResponseEntity<>("Account is Locked ! Please contact admin to unlock it", HttpStatus.BAD_REQUEST);
        }

        return userService.login(loginDto, user);
    }

    @GetMapping("hello")
    public String display() {
        return "hello";
    }


    @PostMapping("customer/register")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerRegisterDto customerRegisterDto) {

        return userService.registerCustomer(customerRegisterDto);

    }

    @PostMapping("seller/register")
    public ResponseEntity<String> registerSeller(@Valid @RequestBody SellerRegisterDto sellerRegisterDto) {

        return userService.registerSeller(sellerRegisterDto);
    }

    @GetMapping("activate_account/{email}/{token}")
    public ResponseEntity<String> confirmAccount(@PathVariable("email") String email, @PathVariable("token") String token) {

        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isPresent()) {
            return userService.confirmAccount(userEntity.get(), token);
        }

        return new ResponseEntity<>("Account with this email do not exists", HttpStatus.BAD_REQUEST);
    }


    @PostMapping("resend/activation-link")
    public String resentActivationLink(@Valid @PathVariable("email") String email) {
        return customerDaoService.resendActivationLink(email);
    }


    /**
     *          Generate New Access Token From RefreshToken
     */
    @GetMapping("resend/accessToken/{refreshToken}")
    public ResponseEntity<?> accessTokenFromRefreshToken(@PathVariable("refreshToken") String refreshToken){

      return  tokenService.newAccessToken(refreshToken);

    }
    @GetMapping("forget-password/{email}")
    public ResponseEntity<?> forgetUserPassword(@PathVariable("email") String email) {
        return userPasswordService.forgetPassword(email);
    }

    @PatchMapping("reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        return userPasswordService.resetUserPassword(resetPasswordDto);
    }

    @GetMapping("logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {

        String email = userService.emailFromToken(request);

        return blackListTokenService.logOutUser(email);
    }


    @GetMapping("/check")
    public String getCheck(){
        return messageSource.getMessage("ecommerce.error.userNotFound ",null, Locale.ENGLISH);
    }

}
