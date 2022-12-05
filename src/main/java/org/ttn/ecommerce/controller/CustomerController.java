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
import org.ttn.ecommerce.services.impl.TokenServiceImpl;
import org.ttn.ecommerce.services.impl.ImageServiceImpl;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(value = "/customer")
public class CustomerController {


    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncode;
    private JWTGenerator jwtGenerator;
    private TokenService tokenService;
    private CustomerService customerService;
    private ImageService imageService;
    private CategoryServiceImpl categoryService;

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
     *
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
    public ResponseEntity<?> listFilesUsingJavaIO(Authentication authentication){

        String email = authentication.getName();
        return imageService.getImage(email);
    }

    /**
     *
     *      @param      authentication
     *      @return
     */
    @GetMapping("view/profile")
    public CustomerResponseDto viewCustomerProfile(Authentication authentication) {
        log.info("{Fetch Email From Authentication Object}");
        String email = authentication.getName();
        return customerService.customerProfile(email);
    }


    /**
     *      @Usage  Update Customer Profile
     *      @param  customer
     *      @param  authentication
     */
    @PatchMapping("update/profile")
    public ResponseEntity<String> updateCustomerProfile(@RequestBody Customer customer, Authentication authentication) {
        String email = authentication.getName();
        return customerService.updateProfile(email, customer);

    }


    /**
     *      @Constraint     Password Should be a Valid And Meet Constraints
     *      @param          customerPasswordDto
     *      @param          authentication
     */
    @PatchMapping("update/password")
    public ResponseEntity<String> updateCustomerPassword(@RequestBody CustomerPasswordDto customerPasswordDto, Authentication authentication) {
        String email = authentication.getName();
        return customerService.updatePassword(customerPasswordDto, email);

    }

    /**
     *      @Usage   Add Customer Address
     *      @param   address
     *      @param   authentication
     */
    @PostMapping("add/address")
    public ResponseEntity<?> addCustomerAddress(@RequestBody Address address, Authentication authentication) {
        String email = authentication.getName();
        return customerService.insertCustomerAddress(email, address);
    }

    /**
     * @param   authentication
     * @return  Users Address
     * @throws  IOException
     */
    @GetMapping("view/address")
    public AddressResponseDto viewAddress(Authentication authentication) throws IOException {
        String email = authentication.getName();

        return customerService.viewCustomerAddresses(email);
    }


    /**
     *      @Usage       Delete Customer Address
     *      @param       id
     *      @param       authentication
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




