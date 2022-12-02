//package org.ttn.ecommerce.services;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.json.MappingJacksonValue;
//import org.ttn.ecommerce.dto.update.SellerPasswordDto;
//import org.ttn.ecommerce.entities.Address;
//import org.ttn.ecommerce.entities.Seller;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//public interface SellerService {
//    String emailFromToken(HttpServletRequest request);
//
//    /*Deactivate Seller*/
//    String deActivateSeller(Long id);
//
//    /* Activate Seller */
//    String activateSeller(Long id);
//
//    /*Seller's profile*/
//    MappingJacksonValue sellerProfile(String email);
//
//    /* Update Profile */
//    ResponseEntity<String> updateProfile(String email, Seller seller);
//
//    /* Add Seller Address */
//    ResponseEntity<?> insertSellerAddress(String email, Address address);
//
//    /*display Seller addresses*/
//    String viewSellerAddresses(String email) throws IOException;
//
//    /* Delete Address by Id */
//    String deleteSellerAddressById(String email, Long id);
//
//    /* Update Seller Address */
//    String updateSellerAddressById(String email, Long id, Address address);
//
//    /*Update Address*/
//    ResponseEntity<String> updateAddress(String email, Seller seller);
//
//    /* Update Sellers Password */
//    ResponseEntity<String> updatePassword(SellerPasswordDto sellerPasswordDto, String email);
//
//    MappingJacksonValue listAllSellers(String pageSize, String pageOffset, String sortBy);
//}
