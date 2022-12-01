package org.ttn.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ttn.ecommerce.dto.update.CustomerPasswordDto;
import org.ttn.ecommerce.entities.Address;
import org.ttn.ecommerce.entities.Customer;
import org.ttn.ecommerce.repository.RoleRepository;
import org.ttn.ecommerce.repository.UserRepository;
import org.ttn.ecommerce.security.JWTGenerator;
import org.ttn.ecommerce.services.categoryService.CategoryService;
import org.ttn.ecommerce.services.CustomerDaoService;
import org.ttn.ecommerce.services.tokenService.TokenService;
import org.ttn.ecommerce.services.image.ImageService;

import javax.servlet.http.HttpServletRequest;
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
    private final CustomerDaoService customerDaoService;
    private final ImageService imageService;

    private CategoryService categoryService;

    @Autowired
    public CustomerController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncode, JWTGenerator jwtGenerator, TokenService tokenService, CustomerDaoService customerDaoService, ImageService imageService, CategoryService categoryService) {
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
    public String uploadImage(@RequestParam("image") MultipartFile image, HttpServletRequest request) throws IOException {

        String email = customerDaoService.emailFromToken(request);

        return imageService.uploadImage(email, image);

    }


    @GetMapping("/view/image")
    public ResponseEntity<?> listFilesUsingJavaIO( HttpServletRequest request){

        String email = customerDaoService.emailFromToken(request);
        return imageService.getImage(email);
    }

    @GetMapping("view/profile")
    public MappingJacksonValue viewCustomerProfile(HttpServletRequest request) {
        String email = customerDaoService.emailFromToken(request);
        return customerDaoService.customerProfile(email);
    }


    @PatchMapping("update/profile")
    public ResponseEntity<String> updateCustomerAddress(@RequestBody Customer customer, HttpServletRequest request) {
        String email = customerDaoService.emailFromToken(request);
        return customerDaoService.updateProfile(email, customer);

    }

    @PatchMapping("update/password")
    public ResponseEntity<String> updateCustomerPassword(@RequestBody CustomerPasswordDto customerPasswordDto, HttpServletRequest request) {
        String email = customerDaoService.emailFromToken(request);
        return customerDaoService.updatePassword(customerPasswordDto, email);

    }

    @PostMapping("add/address")
    public ResponseEntity<?> addCustomerAddress(@RequestBody Address address, HttpServletRequest request) {
        String email = customerDaoService.emailFromToken(request);
        return customerDaoService.insertCustomerAddress(email, address);
    }

    @GetMapping("view/address")
    public MappingJacksonValue viewAddress(HttpServletRequest request) throws IOException {
        String email = customerDaoService.emailFromToken(request);

        return customerDaoService.viewCustomerAddresses(email);
    }

    @DeleteMapping("delete/address/{id}")
    public String deleteCustomerAddress(@PathVariable("id") Long id, HttpServletRequest request) {
        String email = customerDaoService.emailFromToken(request);

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
    public String updateCustomerAddress(@RequestBody Address address, @PathVariable("id") Long id, HttpServletRequest request) {
        String email = customerDaoService.emailFromToken(request);
        return customerDaoService.updateCustomerAddressById(email, id, address);
    }


    /**
     *
     * API to fetch filtering details for a category

            */


}




