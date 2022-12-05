package org.ttn.ecommerce.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ttn.ecommerce.dto.image.ImageResponse;
import org.ttn.ecommerce.dto.responseDto.userDto.AddressResponseDto;
import org.ttn.ecommerce.dto.responseDto.userDto.CustomerResponseDto;
import org.ttn.ecommerce.dto.update.CustomerPasswordDto;
import org.ttn.ecommerce.entity.user.Address;
import org.ttn.ecommerce.entity.user.Customer;
import org.ttn.ecommerce.repository.UserRepository.RoleRepository;
import org.ttn.ecommerce.repository.UserRepository.UserRepository;
import org.ttn.ecommerce.security.JWTGenerator;
import org.ttn.ecommerce.services.CustomerService;
import org.ttn.ecommerce.services.ImageService;
import org.ttn.ecommerce.services.TokenService;
import org.ttn.ecommerce.services.impl.CategoryServiceImpl;
import org.ttn.ecommerce.services.impl.CustomerServiceImpl;
import org.ttn.ecommerce.services.impl.ImageServiceImpl;
import org.ttn.ecommerce.services.impl.TokenServiceImpl;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(value = "/customer")
public class CustomerController {


    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncode;
    private final JWTGenerator jwtGenerator;
    private final TokenService tokenService;
    private final CustomerService customerService;
    private final ImageService imageService;
    private final CategoryServiceImpl categoryService;

    @Autowired
    public CustomerController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncode, JWTGenerator jwtGenerator, TokenServiceImpl tokenService, CustomerServiceImpl customerDaoService, ImageServiceImpl imageService, CategoryServiceImpl categoryService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncode = passwordEncode;
        this.jwtGenerator = jwtGenerator;
        this.tokenService = tokenService;
        this.customerService = customerDaoService;
        this.imageService = imageService;
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("login")
    public String display() {
        return "a";
    }


    /**
     * @param image
     * @param authentication
     * @throws IOException
     */
    @PostMapping(value = "upload/image")
    public ImageResponse uploadImage(@RequestParam("image") MultipartFile image, Authentication authentication) throws IOException {
        log.info("{Fetch Email From Authentication Object}");
        String email = authentication.getName();

        return imageService.uploadImage(email, image);

    }

    /**
     * @param authentication
     */
    @GetMapping("/view/image")
    public ResponseEntity<?> listFilesUsingJavaIO(Authentication authentication) {

        String email = authentication.getName();
        return imageService.getImage(email);
    }

    /**
     * @param authentication
     * @return
     */
    @GetMapping("view/profile")
    public CustomerResponseDto viewCustomerProfile(Authentication authentication) {
        log.info("{Customer Profile}");
        String email = authentication.getName();
        return customerService.customerProfile(email);
    }


    /**
     * @param customer
     * @param authentication
     * @Usage Update Customer Profile
     */
    @PatchMapping("update/profile")
    public ResponseEntity<String> updateCustomerAddress(@RequestBody Customer customer, Authentication authentication) {
        String email = authentication.getName();
        return customerService.updateProfile(email, customer);

    }


    /**
     * @param customerPasswordDto
     * @param authentication
     * @Constraint Password Should be a Valid And Meet Constraints
     */
    @PatchMapping("update/password")
    public ResponseEntity<String> updateCustomerPassword(@RequestBody CustomerPasswordDto customerPasswordDto, Authentication authentication) {
        String email = authentication.getName();
        return customerService.updatePassword(customerPasswordDto, email);

    }

    /**
     * @param address
     * @param authentication
     * @Usage Add Customer Address
     */
    @PostMapping("add/address")
    public ResponseEntity<?> addCustomerAddress(@RequestBody Address address, Authentication authentication) {
        String email = authentication.getName();
        return customerService.insertCustomerAddress(email, address);
    }

    /**
     * @param authentication
     * @return Users Address
     * @throws IOException
     */
    @GetMapping("view/address")
    public AddressResponseDto viewAddress(Authentication authentication) throws IOException {
        String email = authentication.getName();

        return customerService.viewCustomerAddresses(email);
    }


    /**
     * @param id
     * @param authentication
     * @Usage Delete Customer Address
     */
    @DeleteMapping("delete/address/{id}")
    public String deleteCustomerAddress(@PathVariable("id") Long id, Authentication authentication) {
        String email = authentication.getName();

        return customerService.deleteCustomerAddressById(email, id);
    }


    @PatchMapping("/update/address/{id}")
    public String updateCustomerAddress(@RequestBody Address address, @PathVariable("id") Long id, Authentication authentication) {
        String email = authentication.getName();
        return customerService.updateCustomerAddressById(email, id, address);
    }


}




