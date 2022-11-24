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
import org.ttn.ecommerce.services.CustomerDaoService;
import org.ttn.ecommerce.services.TokenService;
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

    @Autowired
    public CustomerController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncode, JWTGenerator jwtGenerator, TokenService tokenService, CustomerDaoService customerDaoService, ImageService imageService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncode = passwordEncode;
        this.jwtGenerator = jwtGenerator;

        this.tokenService = tokenService;
        this.customerDaoService = customerDaoService;
        this.imageService = imageService;
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("login")
    public String display() {
        return "a";
    }


    /*send it to service layer*/
    @PostMapping(value = "upload/image")
    public String uploadImage(@RequestParam("image") MultipartFile image, HttpServletRequest request) throws IOException {

        String email = customerDaoService.emailFromToken(request);

        return imageService.uploadImage(email, image);


//        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
//        Optional<Images> userImage = imageRepository.findByUserId(userEntity.get().getId());
//        if(userImage.isPresent()){
//            imageRepository.delete(userImage.get());
//        }else{
//            imageRepository.save(Images.builder()
//                    .name(image.getOriginalFilename())
//                    .fileType(image.getContentType())
//                   .uploadedAt(LocalDateTime.now())
//                    .userEntity(userEntity.get())
//                    .image(ImageUtility.compressImage(image.getBytes())).build());
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body("Image uploaded Successfully" + image.getOriginalFilename());
//        }
//
//
//        return new ResponseEntity<>("Error in uploading image",HttpStatus.BAD_REQUEST);

    }

    @GetMapping("profile/view")
    public MappingJacksonValue viewCustomerProfile(HttpServletRequest request) {
        String email = customerDaoService.emailFromToken(request);
        return customerDaoService.customerProfile(email);
    }

    @PatchMapping("profile/update")
    public ResponseEntity<String> updateCustomerAddress(@RequestBody Customer customer, HttpServletRequest request) {
        String email = customerDaoService.emailFromToken(request);
        return customerDaoService.updateProfile(email, customer);

    }

    @PatchMapping("update/password")
    public ResponseEntity<String> updateCustomerPassword(@RequestBody CustomerPasswordDto customerPasswordDto, HttpServletRequest request) {
        String email = customerDaoService.emailFromToken(request);
        return customerDaoService.updatePassword(customerPasswordDto, email);

    }
//    @GetMapping("profile/image")
//    public ResponseEntity<byte[]> getImage(HttpServletRequest request) throws IOException {
//
//        System.out.println("a");
//        String email = customerDaoService.emailFromToken(request);
//        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
//
//        Optional<Images> customerImage = imageRepository.findByUserId(userEntity.get().getId());
//
//
//        System.out.println(customerImage.get().getImage());
//        return ResponseEntity
//                .ok()
//                .contentType(MediaType.valueOf(customerImage.get().getFileType()))
//                .body(ImageUtility.decompressImage(customerImage.get().getImage()));
//    }

    @PostMapping("add-address")
    public ResponseEntity<?> addCustomerAddress(@RequestBody Address address, HttpServletRequest request) {
        String email = customerDaoService.emailFromToken(request);
        return customerDaoService.insertCustomerAddress(email, address);
    }

    @GetMapping("view-address")
    public MappingJacksonValue viewAddress(HttpServletRequest request) throws IOException {
        String email = customerDaoService.emailFromToken(request);

        return customerDaoService.viewCustomerAddresses(email);

    }

    @DeleteMapping("delete/address/{id}")
    public String deleteCustomerAddress(@PathVariable("id") Long id, HttpServletRequest request) {
        String email = customerDaoService.emailFromToken(request);

        return customerDaoService.deleteCustomerAddressById(email, id);

    }

    @PatchMapping("/update/address/{id}")
    public String updateCustomerAddress(@RequestBody Address address, @PathVariable("id") Long id, HttpServletRequest request) {
        String email = customerDaoService.emailFromToken(request);
        return customerDaoService.updateCustomerAddressById(email, id, address);
    }


}




