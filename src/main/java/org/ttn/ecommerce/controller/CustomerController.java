package org.ttn.ecommerce.controller;

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
import org.ttn.ecommerce.entities.Address;
import org.ttn.ecommerce.entities.Customer;
import org.ttn.ecommerce.repository.RoleRepository;
import org.ttn.ecommerce.repository.UserRepository;
import org.ttn.ecommerce.security.JWTGenerator;
import org.ttn.ecommerce.services.categoryService.CategoryService;
import org.ttn.ecommerce.services.CustomerServiceImpl;
import org.ttn.ecommerce.services.tokenService.TokenService;
import org.ttn.ecommerce.services.image.ImageService;

import java.io.IOException;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {


    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncode;

    private final JWTGenerator jwtGenerator;

    private final TokenService tokenService;
    private final CustomerServiceImpl customerDaoService;
    private final ImageService imageService;

    private CategoryService categoryService;

    @Autowired
    public CustomerController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncode, JWTGenerator jwtGenerator, TokenService tokenService, CustomerServiceImpl customerDaoService, ImageService imageService, CategoryService categoryService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncode = passwordEncode;
        this.jwtGenerator = jwtGenerator;

        this.tokenService = tokenService;
        this.customerDaoService = customerDaoService;
        this.imageService = imageService;
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("login")
    public String display() {
        return "a";
    }


    @PostMapping(value = "upload/image")
    public ImageResponse uploadImage(@RequestParam("image") MultipartFile image, Authentication authentication) throws IOException {

        String email = authentication.getName();

        return imageService.uploadImage(email, image);

    }


    @GetMapping("/view/image")
    public ResponseEntity<?> listFilesUsingJavaIO(Authentication authentication){

        String email = authentication.getName();
        return imageService.getImage(email);
    }

    @GetMapping("view/profile")
    public CustomerResponseDto viewCustomerProfile(Authentication authentication) {
        String email = authentication.getName();
        return customerDaoService.customerProfile(email);
    }


    @PatchMapping("update/profile")
    public ResponseEntity<String> updateCustomerAddress(@RequestBody Customer customer, Authentication authentication) {
        String email = authentication.getName();
        return customerDaoService.updateProfile(email, customer);

    }

    @PatchMapping("update/password")
    public ResponseEntity<String> updateCustomerPassword(@RequestBody CustomerPasswordDto customerPasswordDto, Authentication authentication) {
        String email = authentication.getName();
        return customerDaoService.updatePassword(customerPasswordDto, email);

    }

    @PostMapping("add/address")
    public ResponseEntity<?> addCustomerAddress(@RequestBody Address address, Authentication authentication) {
        String email = authentication.getName();
        return customerDaoService.insertCustomerAddress(email, address);
    }

    @GetMapping("view/address")
    public AddressResponseDto viewAddress(Authentication authentication) throws IOException {
        String email = authentication.getName();

        return customerDaoService.viewCustomerAddresses(email);
    }

    @DeleteMapping("delete/address/{id}")
    public String deleteCustomerAddress(@PathVariable("id") Long id, Authentication authentication) {
        String email = authentication.getName();

        return customerDaoService.deleteCustomerAddressById(email, id);
    }


    /**
     *      Return List all root level Categories if no ID is passed, else
     *      list of all immediate child nodes of passed category ID
     */
    @GetMapping(value = {"view/categories","view/categories/{id}"})
    public ResponseEntity<?> viewAllCategories(@PathVariable(value = "id",required = false)Long id){

        return  categoryService.listCategoriesOfCustomer(id);
    }


    @PatchMapping("/update/address/{id}")
    public String updateCustomerAddress(@RequestBody Address address, @PathVariable("id") Long id, Authentication authentication) {
        String email = authentication.getName();
        return customerDaoService.updateCustomerAddressById(email, id, address);
    }


    /**
     *
     * API to fetch filtering details for a category

            */


}




