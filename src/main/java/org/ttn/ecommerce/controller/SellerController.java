package org.ttn.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ttn.ecommerce.dto.image.ImageResponse;
import org.ttn.ecommerce.dto.responseDto.categoryResponseDto.SubCategoryResponseDto;
import org.ttn.ecommerce.dto.responseDto.userDto.SellerResponseDto;
import org.ttn.ecommerce.dto.update.SellerPasswordDto;
import org.ttn.ecommerce.entity.user.Address;
import org.ttn.ecommerce.entity.user.Seller;
import org.ttn.ecommerce.services.impl.CategoryService;
import org.ttn.ecommerce.services.impl.SellerService;
import org.ttn.ecommerce.services.impl.ImageService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/seller")

public class SellerController {

    @Autowired
    SellerService sellerDaoService;

    @Autowired
    ImageService imageService;

    @Autowired
    CategoryService categoryService;

    @PreAuthorize("hasRole('ROLE_SELLER')")
    @GetMapping("login")
    public String getData() {
        return "a";
    }

    /**
     * @Problem : View Profile
     * @Output  : Sellers Basic Detail, Profile Image URl And Address
     */
    @GetMapping("view/profile")
    public SellerResponseDto viewSellerProfile(Authentication authentication) {
        String email = authentication.getName();
        return sellerDaoService.sellerProfile(email);
    }

    /**
     * @Problem : Update Seller Profile
     * @param   : Fields To Be Uploaded
     */
    @PatchMapping("update/profile")
    public ResponseEntity<String> updateSellerAddress(@RequestBody Seller seller, Authentication authentication) {
        String email = authentication.getName();
        return sellerDaoService.updateProfile(email, seller);

    }

    /**
     *      @Problem    :   Update Sellers Password
     *      @param      :   <<Username>>  and <<Password>>
     */
    @PatchMapping("update/password")
    public ResponseEntity<String> updateSellerPassword(@RequestBody SellerPasswordDto sellerPasswordDto, Authentication authentication) {
        String email = authentication.getName();
        return sellerDaoService.updatePassword(sellerPasswordDto, email);

    }


    /**
     *      @Problem         : Add Sellers Address
     *      @Constriants     : Seller Should Have Only One Address
     */
    @PostMapping("add/address")
    public ResponseEntity<?> addSellerAddress(@RequestBody Address address, Authentication authentication) {
        String email = authentication.getName();
        return sellerDaoService.insertSellerAddress(email, address);
    }

    /**
     *      @Problem  : View Sellers Address
     */
    @GetMapping("view/address")
    public String viewAddress(Authentication authentication) throws IOException {
        String email = authentication.getName();

        return sellerDaoService.viewSellerAddresses(email);

    }

    /**
     *      @Problem   :   Delete Sellers Address
     *      @Param     :   Seller's Address ID
     */
    @DeleteMapping("delete/address/{id}")
    public String deleteSellerAddress(@PathVariable("id") Long id, Authentication authentication) {
        String email = authentication.getName();

        return sellerDaoService.deleteSellerAddressById(email, id);

    }

    /**
     *      @Problem  :  Update Seller's Address
     *      @Param    :  Seller's Address ID
     */
    @PatchMapping("/update/address/{id}")
    public String updateSellerAddress(@RequestBody Address address, @PathVariable("id") Long id, Authentication authentication) {
        String email = authentication.getName();
        return sellerDaoService.updateSellerAddressById(email, id, address);
    }

    /**
     *      @Problem        : Upload Image
     *      @Param          : Image
     *      @Constraints    : Only Jpeg, Jpg, Png FileTypes Allowed
     */
    @PostMapping(value = "upload/image")
    public ImageResponse uploadImage(@RequestParam("image") MultipartFile image, Authentication authentication) throws IOException {

        String email = authentication.getName();

        return imageService.uploadImage(email, image);
    }

    /**
     *      @Probelem  :  View Image
     */
    @GetMapping("view/image")
    public ResponseEntity<?> listFilesUsingJavaIO(Authentication authentication){

        String email = authentication.getName();
        return imageService.getImage(email);
    }



    @GetMapping("view/all-categories")
    public List<SubCategoryResponseDto> viewAllCategories(){

        return categoryService.viewAllCategory();

    }


}
