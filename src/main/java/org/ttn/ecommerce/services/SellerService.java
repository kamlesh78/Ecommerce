package org.ttn.ecommerce.services;

import org.springframework.http.ResponseEntity;
import org.ttn.ecommerce.dto.responseDto.userDto.SellerResponseDto;
import org.ttn.ecommerce.dto.update.SellerPasswordDto;
import org.ttn.ecommerce.entity.user.Address;
import org.ttn.ecommerce.entity.user.Seller;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface SellerService {
    String emailFromToken(HttpServletRequest request);

    String deActivateSeller(Long id);

    String activateSeller(Long id);

    SellerResponseDto sellerProfile(String email);

    ResponseEntity<String> updateProfile(String email, Seller seller);

    ResponseEntity<?> insertSellerAddress(String email, Address address);

    String viewSellerAddresses(String email) throws IOException;

    String deleteSellerAddressById(String email, Long id);

    String updateSellerAddressById(String email, Long id, Address address);

    ResponseEntity<String> updateAddress(String email, Seller seller);

    ResponseEntity<String> updatePassword(SellerPasswordDto sellerPasswordDto, String email);


    List<SellerResponseDto> listAllSellers(String pageSize, String pageOffset, String sortBy);
}
