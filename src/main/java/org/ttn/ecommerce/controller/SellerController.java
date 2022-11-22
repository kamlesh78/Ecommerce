package org.ttn.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ttn.ecommerce.dto.update.CustomerPasswordDto;
import org.ttn.ecommerce.entities.Address;
import org.ttn.ecommerce.entities.Customer;
import org.ttn.ecommerce.entities.Images;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.repository.SellerRepository;
import org.ttn.ecommerce.services.SellerDaoService;
import org.ttn.ecommerce.services.util.ImageUtility;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping(value = "/seller")
public class SellerController {

    @Autowired
    SellerDaoService sellerDaoService;


    @PreAuthorize("hasRole('ROLE_SELLER')")
    @GetMapping("login")
    public String getData(){
        return "a";
    }

    /*send it to service layer*/
//    @PostMapping(value = "upload/image")
//    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image, HttpServletRequest request) throws IOException {
//
//        String email = customerDaoService.emailFromToken(request);
//        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
//        Optional<Images> userImage = imageRepository.findByUserId(userEntity.get().getId());
//        if(userImage.isPresent()){
//            imageRepository.delete(userImage.get());
//        }else{
//            imageRepository.save(Images.builder()
//                    .name(image.getOriginalFilename())
//                    .fileType(image.getContentType())
//                    .uploadedAt(LocalDateTime.now())
//                    .userEntity(userEntity.get())
//                    .image(ImageUtility.compressImage(image.getBytes())).build());
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body("Image uploaded Successfully" + image.getOriginalFilename());
//        }
//
//
//        return new ResponseEntity<>("Error in uploading image",HttpStatus.BAD_REQUEST);
//
//    }

    @GetMapping("profile/view")
    public MappingJacksonValue viewCustomerProfile(HttpServletRequest request){
        String email = sellerDaoService.emailFromToken(request);
        return sellerDaoService.sellerProfile(email);
    }
//
//    @PatchMapping("profile/update")
//    public ResponseEntity<String> updateCustomerAddress(@RequestBody Customer customer, HttpServletRequest request){
//        String email = customerDaoService.emailFromToken(request);
//        return customerDaoService.updateProfile(email,customer);
//
//    }
//
//    @PatchMapping("update/password")
//    public ResponseEntity<String> updateCustomerPassword(@RequestBody CustomerPasswordDto customerPasswordDto, HttpServletRequest request){
//        String email = customerDaoService.emailFromToken(request);
//        return customerDaoService.updatePassword(customerPasswordDto,email);
//
//    }
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
//
//    @PostMapping("add-address")
//    public ResponseEntity<?> addCustomerAddress(@RequestBody Address address, HttpServletRequest request){
//        String email = customerDaoService.emailFromToken(request);
//        return customerDaoService.insertCustomerAddress(email,address);
//    }
//
//    @GetMapping("view-address")
//    public MappingJacksonValue viewAddress(HttpServletRequest request) throws  IOException{
//        String email = customerDaoService.emailFromToken(request);
//
//        return  customerDaoService.viewCustomerAddresses(email);
//
//    }
//
//    @DeleteMapping("delete/address/{id}")
//    public String deleteCustomerAddress(@PathVariable("id") Long id,HttpServletRequest request){
//        String email = customerDaoService.emailFromToken(request);
//
//        return customerDaoService.deleteCustomerAddressById(email,id);
//
//    }
//
//    @PatchMapping("/update/address/{id}")
//    public String updateCustomerAddress(@RequestBody Address address, @PathVariable("id") Long id,HttpServletRequest request){
//        String email = customerDaoService.emailFromToken(request);
//        return customerDaoService.updateCustomerAddressById(email,id,address);
//    }
//

}
