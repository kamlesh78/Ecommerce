package org.ttn.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ttn.ecommerce.entities.Images;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.repository.ImageRepository;
import org.ttn.ecommerce.repository.RoleRepository;
import org.ttn.ecommerce.repository.UserRepository;
import org.ttn.ecommerce.security.JWTGenerator;
import org.ttn.ecommerce.services.TokenService;
import org.ttn.ecommerce.services.util.ImageUtility;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {



    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncode;

    private JWTGenerator jwtGenerator;
    private ImageRepository imageRepository;
    private TokenService tokenService;


    @Autowired
    public CustomerController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncode, JWTGenerator jwtGenerator, ImageRepository imageRepository, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncode = passwordEncode;
        this.jwtGenerator = jwtGenerator;
        this.imageRepository = imageRepository;
        this.tokenService = tokenService;
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("login")
    public String display(){
        return "a";
    }

    @PostMapping(value = "upload/image")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image,HttpServletRequest request) throws IOException {
        String token = tokenService.getJWTFromRequest(request);
        String email = tokenService.getUsernameFromJWT(token);

        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        Optional<Images> userImage = imageRepository.findByUserId(userEntity.get().getId());
        if(userImage.isPresent()){
            imageRepository.delete(userImage.get());
        }else{
            imageRepository.save(Images.builder()
                    .name(image.getOriginalFilename())
                    .fileType(image.getContentType())
                    .uploadedAt(LocalDateTime.now())
                    .userEntity(userEntity.get())
                    .image(ImageUtility.compressImage(image.getBytes())).build());
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Image uploaded Successfully" + image.getOriginalFilename());
        }


        return new ResponseEntity<>("Error in uploading image",HttpStatus.BAD_REQUEST);

    }

    @GetMapping("profile/image")
    public ResponseEntity<byte[]> getImage(HttpServletRequest request) throws IOException {

        System.out.println("a");
        String token = tokenService.getJWTFromRequest(request);
        String email = tokenService.getUsernameFromJWT(token);
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);

        Optional<Images> customerImage = imageRepository.findByUserId(userEntity.get().getId());


        System.out.println(customerImage.get().getImage());
        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(customerImage.get().getFileType()))
                .body(ImageUtility.decompressImage(customerImage.get().getImage()));
    }
}




