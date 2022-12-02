package org.ttn.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ttn.ecommerce.dto.image.ImageResponse;
import org.ttn.ecommerce.dto.responseDto.userDto.SellerResponseDto;
import org.ttn.ecommerce.dto.update.SellerPasswordDto;
import org.ttn.ecommerce.entities.Address;
import org.ttn.ecommerce.entities.Seller;
import org.ttn.ecommerce.services.categoryService.CategoryService;
import org.ttn.ecommerce.services.SellerServiceImpl;
import org.ttn.ecommerce.services.image.ImageService;

import java.io.IOException;

@RestController
@RequestMapping(value = "/seller")

public class SellerController {

    @Autowired
    SellerServiceImpl sellerDaoService;

    @Autowired
    ImageService imageService;

    @Autowired
    CategoryService categoryService;

    @PreAuthorize("hasRole('ROLE_SELLER')")
    @GetMapping("login")
    public String getData() {
        return "a";
    }


    @GetMapping("view/profile")
    public SellerResponseDto viewSellerProfile(Authentication authentication) {
        String email = authentication.getName();
        return sellerDaoService.sellerProfile(email);
    }

    @PatchMapping("update/profile")
    public ResponseEntity<String> updateSellerAddress(@RequestBody Seller seller, Authentication authentication) {
        String email = authentication.getName();
        return sellerDaoService.updateProfile(email, seller);

    }

    @PatchMapping("update/password")
    public ResponseEntity<String> updateSellerPassword(@RequestBody SellerPasswordDto sellerPasswordDto, Authentication authentication) {
        String email = authentication.getName();
        return sellerDaoService.updatePassword(sellerPasswordDto, email);

    }


    /**
     *       Seller Address Constraint :-> Seller should have only one address
     *  */
    @PostMapping("add/address")
    public ResponseEntity<?> addSellerAddress(@RequestBody Address address, Authentication authentication) {
        String email = authentication.getName();
        return sellerDaoService.insertSellerAddress(email, address);
    }

    @GetMapping("view/address")
    public String viewAddress(Authentication authentication) throws IOException {
        String email = authentication.getName();

        return sellerDaoService.viewSellerAddresses(email);

    }

    @DeleteMapping("delete/address/{id}")
    public String deleteSellerAddress(@PathVariable("id") Long id, Authentication authentication) {
        String email = authentication.getName();

        return sellerDaoService.deleteSellerAddressById(email, id);

    }

    @PatchMapping("/update/address/{id}")
    public String updateSellerAddress(@RequestBody Address address, @PathVariable("id") Long id, Authentication authentication) {
        String email = authentication.getName();
        return sellerDaoService.updateSellerAddressById(email, id, address);
    }


    @PostMapping(value = "upload/image")
    public ImageResponse uploadImage(@RequestParam("image") MultipartFile image, Authentication authentication) throws IOException {

        String email = authentication.getName();

        return imageService.uploadImage(email, image);
    }

    @GetMapping("view/image")
    public ResponseEntity<?> listFilesUsingJavaIO(Authentication authentication){

        String email = authentication.getName();
        return imageService.getImage(email);
    }



    @GetMapping("view/all-categories")
    public ResponseEntity<?> viewAllCategories(){

        return categoryService.viewAllCategory();

    }

}
