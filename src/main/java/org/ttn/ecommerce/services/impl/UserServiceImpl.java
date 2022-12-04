package org.ttn.ecommerce.services.impl;

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
import org.springframework.transaction.annotation.Transactional;
import org.ttn.ecommerce.dto.AuthResponseDto;
import org.ttn.ecommerce.dto.LoginDto;
import org.ttn.ecommerce.dto.register.CustomerRegisterDto;
import org.ttn.ecommerce.dto.register.SellerRegisterDto;
import org.ttn.ecommerce.entity.token.RefreshToken;
import org.ttn.ecommerce.entity.user.*;
import org.ttn.ecommerce.repository.TokenRepository.AccessTokenRepository;
import org.ttn.ecommerce.repository.TokenRepository.RefreshTokenRepository;
import org.ttn.ecommerce.repository.UserRepository.CustomerRepository;
import org.ttn.ecommerce.repository.UserRepository.RoleRepository;
import org.ttn.ecommerce.repository.UserRepository.SellerRepository;
import org.ttn.ecommerce.repository.UserRepository.UserRepository;
import org.ttn.ecommerce.security.JWTGenerator;
import org.ttn.ecommerce.security.SecurityConstants;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;

@Service
@Transactional
@NoArgsConstructor
public class UserServiceImpl implements org.ttn.ecommerce.services.UserService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncode;
    private JWTGenerator jwtGenerator;
    private CustomerRepository customerRepository;
    private EmailServicetry emailServicetry;
    private SellerRepository sellerRepository;
    private TokenServiceImpl tokenService;
    private AccessTokenRepository accessTokenRepository;
    private RefreshTokenRepository refreshTokenRepository;


    @Autowired
    public UserServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncode, JWTGenerator jwtGenerator, CustomerRepository customerRepository, EmailServicetry emailServicetry, SellerRepository sellerRepository, TokenServiceImpl tokenService, AccessTokenRepository accessTokenRepository, RefreshTokenRepository refreshTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncode = passwordEncode;
        this.jwtGenerator = jwtGenerator;
        this.customerRepository = customerRepository;
        this.emailServicetry = emailServicetry;
        this.sellerRepository = sellerRepository;
        this.tokenService = tokenService;
        this.accessTokenRepository = accessTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }


    @Override
    public String emailFromToken(HttpServletRequest request) {
        String token = tokenService.getJWTFromRequest(request);
        String email = tokenService.getUsernameFromJWT(token);
        return email;
    }

    @Override
    public ResponseEntity<String> registerCustomer(CustomerRegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return new ResponseEntity<>("Email is already taken", HttpStatus.BAD_REQUEST);
        }
        Customer customer = new Customer();
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

        String toMail = customer.getEmail();
        String subject = "Your Account || " + customer.getFirstName() + " finish setting up your new  Account ";
        String message = "Click on the link to Activate Your Account \n"
                + "127.0.0.1:8080/api/public/activate_account/" + customer.getEmail() + "/" + token;

        emailServicetry.sendEmail(toMail, subject, message);

        return new ResponseEntity<>("Customer Registered Successfully!Activate Your Account within 3 hours", HttpStatus.CREATED);

    }


    @Override
    public ResponseEntity<String> registerSeller(SellerRegisterDto sellerRegisterDto) {
        if (userRepository.existsByEmail(sellerRegisterDto.getEmail())) {
            return new ResponseEntity<>("Email is already taken", HttpStatus.BAD_REQUEST);
        }
        Seller seller = new Seller();
        seller.setFirstName(sellerRegisterDto.getFirstName());
        seller.setMiddleName(sellerRegisterDto.getMiddleName());
        seller.setLastName(sellerRegisterDto.getLastName());

        seller.setCompanyContact(sellerRegisterDto.getCompanyContact());
        seller.setCompanyName(sellerRegisterDto.getCompanyName());
        seller.setGst(sellerRegisterDto.getGstNumber());

        seller.setActive(false);
        seller.setDeleted(false);
        seller.setExpired(false);
        seller.setLocked(false);
        seller.setInvalidAttemptCount(0);

        seller.setEmail(sellerRegisterDto.getEmail());
        seller.setPassword(passwordEncode.encode(sellerRegisterDto.getPassword()));

        Role roles = roleRepository.findByAuthority("ROLE_SELLER").get();
        System.out.println(roles.getAuthority());
        seller.setRoles(Collections.singletonList(roles));

        sellerRepository.save(seller);

        /** Send Mail to Seller */

        String toMail = seller.getEmail();
        String subject = seller.getFirstName() + " Your Account || " + " has been created! ";
        String message = "Please Kindly wait for admin to approve your account";

        emailServicetry.sendEmail(toMail, subject, message);


        /** Sending mail to Admin user to activate Sellers Account*/

        toMail = SecurityConstants.ADMIN_EMAIL_ADDRESS;
        subject = "New Seller Registered || Activate Sellers Account";
        message = "Seller Id : " + seller.getId() +
                "\nSeller Name : " + seller.getFirstName() +
                "\nSeller Gst Number : " + seller.getGst() +
                "\nReview and Activate Seller's Account";

        emailServicetry.sendEmail(toMail, subject, message);


        return new ResponseEntity<>("Seller Registered Successfully !! kindly wait for admin to approve your seller account", HttpStatus.OK);


    }


    @Override
    public ResponseEntity<?> login(LoginDto loginDto, UserEntity user) {

        if (!passwordEncode.matches(loginDto.getPassword(), user.getPassword())) {
            int count = user.getInvalidAttemptCount() + 1;
            user.setInvalidAttemptCount(count);
            userRepository.saveInvalidCount(user.getInvalidAttemptCount(), user.getId());


            if (user.getInvalidAttemptCount() >= 3) {
                user.setLocked(true);
                userRepository.lockAccount(user.getId());


                String toMail = SecurityConstants.ADMIN_EMAIL_ADDRESS;
                String subject = "User Account Locked!";
                String message = "User Account Has Been Locked After " +
                        +SecurityConstants.MAX_LOGIN_ATTEMPT +
                        " Unsuccessful Login Attempts" + "\n"
                        + "User Id : " + user.getId();

                emailServicetry.sendEmail(toMail, subject, message);


                return new ResponseEntity<>("Your account got locked! Contact Admin to activate it.", HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>("Password is wrong Your  Have <<" + (SecurityConstants.MAX_LOGIN_ATTEMPT - user.getInvalidAttemptCount()) + ">> Attempts Remaining", HttpStatus.UNAUTHORIZED);
        } else {

            userRepository.saveInvalidCount(0, user.getId());


            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);


            /* Access Token */
            Token accessToken = new Token();
            accessToken.setUserEntity(user);
            accessToken.setToken(token);
            accessToken.setCreatedAt(LocalDateTime.now());
            accessToken.setExpiredAt(LocalDateTime.now().plusMinutes(SecurityConstants.JWT_EXPIRATION));
            accessTokenRepository.save(accessToken);


            /* Refresh Token */
            RefreshToken refreshToken = tokenService.generateRefreshToken(user);
            refreshTokenRepository.save(refreshToken);


            return new ResponseEntity<>(new AuthResponseDto(accessToken.getToken(), refreshToken.getToken()), HttpStatus.OK);

        }


    }

    @Override
    public ResponseEntity<String> confirmAccount(UserEntity userEntity, String token) {
        String out = tokenService.confirmAccount(userEntity.getId(), token);
        return new ResponseEntity<>(out, HttpStatus.OK);
    }


}
