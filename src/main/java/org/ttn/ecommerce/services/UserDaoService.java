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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.ttn.ecommerce.dto.AuthResponseDto;
import org.ttn.ecommerce.dto.LoginDto;
import org.ttn.ecommerce.dto.register.CustomerRegisterDto;
import org.ttn.ecommerce.dto.register.SellerRegisterDto;
import org.ttn.ecommerce.entities.*;
import org.ttn.ecommerce.repository.CustomerRepository;
import org.ttn.ecommerce.repository.RoleRepository;
import org.ttn.ecommerce.repository.SellerRepository;
import org.ttn.ecommerce.repository.UserRepository;
import org.ttn.ecommerce.security.JWTGenerator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@NoArgsConstructor
public class UserDaoService {
    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncode;
    private JWTGenerator jwtGenerator;
    private CustomerRepository customerRepository;

    private SellerRepository sellerRepository;

    @Autowired
    public UserDaoService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncode, JWTGenerator jwtGenerator, CustomerRepository customerRepository, SellerRepository sellerRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncode = passwordEncode;
        this.jwtGenerator = jwtGenerator;
        this.customerRepository = customerRepository;
        this.sellerRepository = sellerRepository;
    }




    public ResponseEntity<String> registerCustomer(CustomerRegisterDto registerDto){
        if(userRepository.existsByEmail(registerDto.getEmail())){
            return new ResponseEntity<>("Email is already taken", HttpStatus.BAD_REQUEST);
        }
        Customer customer =new Customer();
        customer.setFirstName(registerDto.getFirstName());
        customer.setMiddleName(registerDto.getMiddleName());
        customer.setLastName(registerDto.getLastName());

        customer.setEmail(registerDto.getEmail());
        customer.setPassword(passwordEncode.encode(registerDto.getPassword()));
        customer.setContact(registerDto.getContact());

        Role roles = roleRepository.findByAuthority("ROLE_CUSTOMER").get();
        System.out.println(roles.getAuthority());
        customer.setRoles(Collections.singletonList(roles));


        //customer.setAddresses(addressList);

        customerRepository.save(customer);

        return new ResponseEntity<>("Customer Registered Successfully",HttpStatus.OK);

    }




    public ResponseEntity<String> registerSeller(SellerRegisterDto sellerRegisterDto){
        if(userRepository.existsByEmail(sellerRegisterDto.getEmail())){
            return new ResponseEntity<>("Email is already taken", HttpStatus.BAD_REQUEST);
        }
        Seller seller =new Seller();
        seller.setFirstName(sellerRegisterDto.getFirstName());
        seller.setMiddleName(sellerRegisterDto.getMiddleName());
        seller.setLastName(sellerRegisterDto.getLastName());

        seller.setEmail(sellerRegisterDto.getEmail());
        seller.setPassword(passwordEncode.encode(sellerRegisterDto.getPassword()));
        seller.setCompanyContact(sellerRegisterDto.getCompanyContact());

        seller.setCompanyName("Kam");
        seller.setGst("12345kkkkl");
        Role roles = roleRepository.findByAuthority("ROLE_SELLER").get();
        System.out.println(roles.getAuthority());
        seller.setRoles(Collections.singletonList(roles));


        //customer.setAddresses(addressList);

        sellerRepository.save(seller);

        return new ResponseEntity<>("Seller Registered Successfully",HttpStatus.OK);

    }


    public ResponseEntity<?> loginCustomer(LoginDto loginDto){


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token),HttpStatus.OK);
    }
}
