package org.ttn.ecommerce.services;

import org.springframework.http.ResponseEntity;
import org.ttn.ecommerce.dto.responseDto.userDto.AddressResponseDto;
import org.ttn.ecommerce.dto.responseDto.userDto.CustomerResponseDto;
import org.ttn.ecommerce.dto.update.CustomerPasswordDto;
import org.ttn.ecommerce.entity.user.Address;
import org.ttn.ecommerce.entity.user.Customer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface CustomerService {
    String emailFromToken(HttpServletRequest request);

    List<CustomerResponseDto> listAllCustomers(String pageSize, String pageOffset, String sortBy);

    /*      Customer Profile        */
    CustomerResponseDto customerProfile(String email);

    /*      Add customer address        */
    ResponseEntity<?> insertCustomerAddress(String email, Address address);

    /*      display customer addresses      */
    AddressResponseDto viewCustomerAddresses(String email) throws IOException;

    String deleteCustomerAddressById(String email, Long id);

    /*       Update Address        */
    String updateCustomerAddressById(String email, Long id, Address address);

    /*     Update Profile         */
    ResponseEntity<String> updateProfile(String email, Customer customer);

    /*      Update Password         */
    ResponseEntity<String> updatePassword(CustomerPasswordDto customerPasswordDto, String email) throws Exception;

    /*       Deactivate Customer          */
    String deActiveCustomer(Long id);

    /*   Activate Customer   */
    String activeCustomer(Long id);

    String resendActivationLink(String email);
}
