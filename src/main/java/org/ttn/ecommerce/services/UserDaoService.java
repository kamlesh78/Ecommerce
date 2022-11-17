package org.ttn.ecommerce.services;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ttn.ecommerce.dto.AuthResponseDto;
import org.ttn.ecommerce.dto.LoginDto;
import org.ttn.ecommerce.dto.register.CustomerRegisterDto;
import org.ttn.ecommerce.dto.register.SellerRegisterDto;
import org.ttn.ecommerce.entities.*;
import org.ttn.ecommerce.repository.CustomerRepository;
import org.ttn.ecommerce.repository.RoleRepository;
import org.ttn.ecommerce.repository.SellerRepository;
import org.ttn.ecommerce.repository.TokenRepository.AccessTokenRepository;
import org.ttn.ecommerce.repository.TokenRepository.RefreshTokenRepository;
import org.ttn.ecommerce.repository.UserRepository;
import org.ttn.ecommerce.security.JWTGenerator;
import org.ttn.ecommerce.security.SecurityConstants;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@NoArgsConstructor
public class UserDaoService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncode;
    private JWTGenerator jwtGenerator;
    private CustomerRepository customerRepository;
    private EmailService emailService;
    private SellerRepository customer;
    private TokenService tokenService;
    private AccessTokenRepository accessTokenRepository;
    private RefreshTokenRepository refreshTokenRepository;


    @Autowired
    public UserDaoService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncode, JWTGenerator jwtGenerator, CustomerRepository customerRepository, EmailService emailService, SellerRepository customer, TokenService tokenService, AccessTokenRepository accessTokenService, RefreshTokenRepository refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncode = passwordEncode;
        this.jwtGenerator = jwtGenerator;
        this.customerRepository = customerRepository;
        this.emailService = emailService;
        this.customer = customer;
        this.tokenService=tokenService;
        this.accessTokenRepository =accessTokenService;
        this.refreshTokenRepository =refreshTokenService;
    }

    public ResponseEntity<String> registerCustomer(CustomerRegisterDto registerDto){
        if(userRepository.existsByEmail(registerDto.getEmail())){
            return new ResponseEntity<>("Email is already taken", HttpStatus.BAD_REQUEST);
        }
        Customer customer =new Customer();
        customer.setFirstName(registerDto.getFirstName());
        customer.setMiddleName(registerDto.getMiddleName());
        customer.setLastName(registerDto.getLastName());

        customer.setActive(false);
        customer.setDeleted(false);
        customer.setExpired(false);
        customer.setLocked(false);
        customer.setInvalidAttemptCount(0);


        customer.setEmail(registerDto.getEmail());
        customer.setPassword(passwordEncode.encode(registerDto.getPassword()));
        customer.setContact(registerDto.getContact());


        Role roles = roleRepository.findByAuthority("ROLE_CUSTOMER").get();
        System.out.println(roles.getAuthority());
        customer.setRoles(Collections.singletonList(roles));

        customerRepository.save(customer);

        String token = tokenService.generateRegisterToken(customer);

        emailService.setSubject("Your Account || "+ customer.getFirstName() + " finish setting up your new  Account " );

        emailService.setToEmail(customer.getEmail());
        emailService.setMessage("Click on the link to Activate Your Account " + token);
        emailService.sendEmail();


        return new ResponseEntity<>("Customer Registered Successfully!Activate Your Account within 3 hours",HttpStatus.CREATED);

    }




    public ResponseEntity<String> registerSeller(SellerRegisterDto sellerRegisterDto){
        if(userRepository.existsByEmail(sellerRegisterDto.getEmail())){
            return new ResponseEntity<>("Email is already taken", HttpStatus.BAD_REQUEST);
        }
        Seller seller =new Seller();
        seller.setFirstName(sellerRegisterDto.getFirstName());
        seller.setMiddleName(sellerRegisterDto.getMiddleName());
        seller.setLastName(sellerRegisterDto.getLastName());

        seller.setActive(false);
        seller.setDeleted(false);
        seller.setExpired(false);
        seller.setLocked(false);
        seller.setInvalidAttemptCount(0);


        seller.setEmail(sellerRegisterDto.getEmail());
        seller.setPassword(passwordEncode.encode(sellerRegisterDto.getPassword()));

        seller.setCompanyContact(sellerRegisterDto.getCompanyContact());
        seller.setCompanyName("Kam");
        seller.setGst("12345kkkkl");


        Role roles = roleRepository.findByAuthority("ROLE_SELLER").get();
        System.out.println(roles.getAuthority());
        seller.setRoles(Collections.singletonList(roles));


        return new ResponseEntity<>("Seller Registered Successfully",HttpStatus.OK);

    }


    public ResponseEntity<?> loginCustomer(LoginDto loginDto,UserEntity user){


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);


        /* Access Token */
        Token accessToken = new Token();
        accessToken.setUserEntity(user);
        accessToken.setToken(token);
        accessToken.setCreatedAt(LocalDateTime.now());
        accessToken.setExpiredAt(LocalDateTime.now().plusMinutes(SecurityConstants.REGISTER_TOKEN_EXPIRE_MIN));
        accessTokenRepository.save(accessToken);


        /* Refresh Token */
        RefreshToken refreshToken = tokenService.generateRefreshToken(user);
        refreshTokenRepository.save(refreshToken);


        return new ResponseEntity<>(new AuthResponseDto(accessToken.getToken(),refreshToken.getToken()),HttpStatus.OK);
    }

    public void confirmAccount(UserEntity userEntity, String token) {
        tokenService.confirmAccount(userEntity.getId(),token);
    }
}
