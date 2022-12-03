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
import org.ttn.ecommerce.entity.Address;
import org.ttn.ecommerce.entity.Customer;
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


    /**
     *
     * @param image
     * @param authentication
     * @throws IOException
     */
    @PostMapping(value = "upload/image")
    public ImageResponse uploadImage(@RequestParam("image") MultipartFile image, Authentication authentication) throws IOException {

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
        String email = authentication.getName();
        return customerDaoService.customerProfile(email);
    }


    /**
     *      @Usage  Update Customer Profile
     *      @param  customer
     *      @param  authentication
     */
    @PatchMapping("update/profile")
    public ResponseEntity<String> updateCustomerAddress(@RequestBody Customer customer, Authentication authentication) {
        String email = authentication.getName();
        return customerDaoService.updateProfile(email, customer);

    }


    /**
     *      @Constraint     Password Should be a Valid And Meet Constraints
     *      @param          customerPasswordDto
     *      @param          authentication
     */
    @PatchMapping("update/password")
    public ResponseEntity<String> updateCustomerPassword(@RequestBody CustomerPasswordDto customerPasswordDto, Authentication authentication) {
        String email = authentication.getName();
        return customerDaoService.updatePassword(customerPasswordDto, email);

    }

    /**
     *      @Usage   Add Customer Address
     *      @param   address
     *      @param   authentication
     */
    @PostMapping("add/address")
    public ResponseEntity<?> addCustomerAddress(@RequestBody Address address, Authentication authentication) {
        String email = authentication.getName();
        return customerDaoService.insertCustomerAddress(email, address);
    }

    /**
     * @param   authentication
     * @return  Users Address
     * @throws  IOException
     */
    @GetMapping("view/address")
    public AddressResponseDto viewAddress(Authentication authentication) throws IOException {
        String email = authentication.getName();

        return customerDaoService.viewCustomerAddresses(email);
    }


    /**
     *      @Usage       Delete Customer Address
     *      @param       id
     *      @param       authentication
     */
    @DeleteMapping("delete/address/{id}")
    public String deleteCustomerAddress(@PathVariable("id") Long id, Authentication authentication) {
        String email = authentication.getName();

        return customerDaoService.deleteCustomerAddressById(email, id);
    }




    @PatchMapping("/update/address/{id}")
    public String updateCustomerAddress(@RequestBody Address address, @PathVariable("id") Long id, Authentication authentication) {
        String email = authentication.getName();
        return customerDaoService.updateCustomerAddressById(email, id, address);
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping(value = {"view/categories","view/categories/{id}"})
    public ResponseEntity<?> viewAllCategories(@PathVariable(value = "id",required = false)Long id){

        return  categoryService.listCategoriesOfCustomer(id);
    }

}




