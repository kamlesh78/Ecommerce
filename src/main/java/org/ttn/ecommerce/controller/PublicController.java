package org.ttn.ecommerce.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.ttn.ecommerce.dto.LoginDto;
import org.ttn.ecommerce.dto.register.CustomerRegisterDto;
import org.ttn.ecommerce.dto.register.SellerRegisterDto;
import org.ttn.ecommerce.dto.reset.ResetPasswordDto;
import org.ttn.ecommerce.entity.user.UserEntity;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.UserRepository.RoleRepository;
import org.ttn.ecommerce.repository.UserRepository.UserRepository;
import org.ttn.ecommerce.security.JWTGenerator;
import org.ttn.ecommerce.services.*;
import org.ttn.ecommerce.services.impl.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/api/public")
public class PublicController {


    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncode;
    private final JWTGenerator jwtGenerator;
    private final UserService userService;
    private final UserPasswordService userPasswordService;
    private final TokenService tokenService;
    private final LogoutService blackListTokenService;
    private final CustomerService customerDaoService;
    private final MessageSource messageSource;

    @Autowired
    public PublicController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncode, JWTGenerator jwtGenerator, UserServiceImpl userService, UserPasswordServiceImpl userPasswordService, TokenServiceImpl tokenService, LogoutServiceImpl blackListTokenService, CustomerServiceImpl customerDaoService, MessageSource messageSource) {
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

    @GetMapping("i18n")
    public String getMessage(Locale locale){
        return (messageSource.getMessage("api.error.emailExist",null,locale));
    }

    /**
     * @param loginDto
     * @return
     * @Consumers <<Admin>>, <<Customer>>, <<Seller>>
     */
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto,Locale locale) {
        log.info("Trying user Sign in");
        UserEntity user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new UserNotFoundException("User with this email not found"));

        if (!user.isActive()) {
            return new ResponseEntity<>(messageSource.getMessage("api.error.accountNotActive",null,locale), HttpStatus.BAD_REQUEST);
        }

        if (user.isLocked()) {
            return new ResponseEntity<>(messageSource.getMessage("api.error.accountLocked",null,locale), HttpStatus.BAD_REQUEST);
        }

        return userService.login(loginDto, user);
    }


    /**
     * @param customerRegisterDto
     * @Usage Customer Registration
     */
    @PostMapping("customer/register")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerRegisterDto customerRegisterDto) {

        return userService.registerCustomer(customerRegisterDto);

    }


    /**
     * @param sellerRegisterDto
     * @Usage Seller Register
     */
    @PostMapping("seller/register")
    public ResponseEntity<String> registerSeller(@Valid @RequestBody SellerRegisterDto sellerRegisterDto) {

        return userService.registerSeller(sellerRegisterDto);
    }


    /**
     * @param email
     * @param token
     * @Usage Activate Users Account Using Email and Token
     */
    @GetMapping("activate_account/{email}/{token}")
    public ResponseEntity<String> confirmAccount(@PathVariable("email") String email, @PathVariable("token") String token) {

        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isPresent()) {
            return userService.confirmAccount(userEntity.get(), token);
        }

        return new ResponseEntity<>("Account with this email do not exists", HttpStatus.BAD_REQUEST);
    }


    /**
     * @param email
     * @return Resend Account Activation Token To User
     */
    @PostMapping("resend/activation-link")
    public String resentActivationLink(@Valid @PathVariable("email") String email) {
        return customerDaoService.resendActivationLink(email);
    }

    /**
     * @Consumers :  <<Admin>>, <<Customer>>, <<Seller>>
     * @Usage :  To Generate New AccessToken Using Refresh Token
     */
    @GetMapping("resend/accessToken/{refreshToken}")
    public ResponseEntity<?> accessTokenFromRefreshToken(@PathVariable("refreshToken") String refreshToken) {

        return tokenService.newAccessToken(refreshToken);

    }

    /**
     * @Consumers : <<Admin>>, <<Customer>>, <<Seller>>
     * @Usage : Reset Current Password Of User
     */
    @GetMapping("forget-password/{email}")
    public ResponseEntity<?> forgetUserPassword(@PathVariable("email") String email) {
        return userPasswordService.forgetPassword(email);
    }

    /**
     * @Param : Reset Password Token, New Password
     */

    @PatchMapping("reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {

        return userPasswordService.resetUserPassword(resetPasswordDto);
    }

    /**
     * @Consumers :  <<Admin>> , <<Customer>> , <<Seller>>
     */
    @GetMapping("logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {

        String email = userService.emailFromToken(request);

        return blackListTokenService.logOutUser(email);
    }


}
