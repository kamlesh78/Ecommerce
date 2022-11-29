package org.ttn.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ttn.ecommerce.dto.update.SellerPasswordDto;
import org.ttn.ecommerce.entities.Address;
import org.ttn.ecommerce.entities.Seller;
import org.ttn.ecommerce.services.CategoryService;
import org.ttn.ecommerce.services.SellerDaoService;
import org.ttn.ecommerce.services.image.ImageService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping(value = "/seller")

public class SellerController {

    @Autowired
    SellerDaoService sellerDaoService;

    @Autowired
    ImageService imageService;

    @Autowired
    CategoryService categoryService;

    @PreAuthorize("hasRole('ROLE_SELLER')")
    @GetMapping("login")
    public String getData() {
        return "a";
    }


    @GetMapping("profile/view")
    public MappingJacksonValue viewSellerProfile(HttpServletRequest request) {
        String email = sellerDaoService.emailFromToken(request);
        return sellerDaoService.sellerProfile(email);
    }

    @PatchMapping("update/profile")
    public ResponseEntity<String> updateSellerAddress(@RequestBody Seller seller, HttpServletRequest request) {
        String email = sellerDaoService.emailFromToken(request);
        return sellerDaoService.updateProfile(email, seller);

    }

    @PatchMapping("update/password")
    public ResponseEntity<String> updateSellerPassword(@RequestBody SellerPasswordDto sellerPasswordDto, HttpServletRequest request) {
        String email = sellerDaoService.emailFromToken(request);
        return sellerDaoService.updatePassword(sellerPasswordDto, email);

    }


    /*Seller Address Constraint :-> Seller should have only one address*/
    @PostMapping("add/address")
    public ResponseEntity<?> addSellerAddress(@RequestBody Address address, HttpServletRequest request) {
        String email = sellerDaoService.emailFromToken(request);
        return sellerDaoService.insertSellerAddress(email, address);
    }

    @GetMapping("view/address")
    public String viewAddress(HttpServletRequest request) throws IOException {
        String email = sellerDaoService.emailFromToken(request);

        return sellerDaoService.viewSellerAddresses(email);

    }

    @DeleteMapping("delete/address/{id}")
    public String deleteSellerAddress(@PathVariable("id") Long id, HttpServletRequest request) {
        String email = sellerDaoService.emailFromToken(request);

        return sellerDaoService.deleteSellerAddressById(email, id);

    }

    @PatchMapping("/update/address/{id}")
    public String updateSellerAddress(@RequestBody Address address, @PathVariable("id") Long id, HttpServletRequest request) {
        String email = sellerDaoService.emailFromToken(request);
        return sellerDaoService.updateSellerAddressById(email, id, address);
    }


    @PostMapping(value = "upload/image")
    public String uploadImage(@RequestParam("image") MultipartFile image, HttpServletRequest request) throws IOException {

        String email = sellerDaoService.emailFromToken(request);

        return imageService.uploadImage(email, image);
    }

    @GetMapping("view/image")
    public ResponseEntity<?> listFilesUsingJavaIO( HttpServletRequest request){

        String email = sellerDaoService.emailFromToken(request);
        return imageService.getImage(email);
    }



    @GetMapping("view/all-categories")
    public ResponseEntity<?> viewAllCategories(){

        return categoryService.viewAllCategory();

    }
}
