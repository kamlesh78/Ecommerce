package org.ttn.ecommerce.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.ttn.ecommerce.dto.responseDto.userDto.CustomerResponseDto;
import org.ttn.ecommerce.dto.responseDto.userDto.SellerResponseDto;
import org.ttn.ecommerce.services.CustomerService;
import org.ttn.ecommerce.services.ProductService;
import org.ttn.ecommerce.services.SellerService;
import org.ttn.ecommerce.services.impl.SellerServiceImpl;
import org.ttn.ecommerce.services.impl.ProductServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private ProductService productService;

    /*  List Seller's address, User can provide Custom  pagination and sorting fields */
    @GetMapping("list/customers")
    public List<CustomerResponseDto> listAllCustomer(
            @RequestParam(name = "pageSize", required = false) String pageSize,
            @RequestParam(name = "pageOffset", required = false) String pageOffset,
            @RequestParam(name = "sort", required = false) String sortBy
    ) throws JsonProcessingException {

        String customPageSize = pageSize == null ? "10" : pageSize;
        String customPageOffset = pageOffset == null ? "0" : pageOffset;
        String customSortBy = sortBy == null ? "email" : sortBy;
        return customerService.listAllCustomers(customPageSize, customPageOffset, customSortBy);

    }


    /* List Seller's address
     *  User can provide Custom pagination and sorting fields
     */

    @GetMapping("list/sellers")
    public List<SellerResponseDto> listAllSeller(
            @RequestParam(name = "pageSize", required = false) String pageSize,
            @RequestParam(name = "pageOffset", required = false) String pageOffset,
            @RequestParam(name = "sort", required = false) String sortBy) throws JsonProcessingException {


        /**
         *         Default Values if Custom Pagination and Sorting fields not provided
         */
        String customPageSize = pageSize == null ? "10" : pageSize;
        String customPageOffset = pageOffset == null ? "0" : pageOffset;
        String customSortBy = sortBy == null ? "email" : sortBy;
        return sellerService.listAllSellers(customPageSize, customPageOffset, customSortBy);
    }

    @PatchMapping("deactivate/customer/{id}")
    public String deActiveCustomer(@PathVariable("id") Long id) {

        return customerService.deActiveCustomer(id);
    }

    @PatchMapping("deactivate/seller/{id}")
    public String deActiveSeller(@PathVariable("id") Long id) {

        return sellerService.deActivateSeller(id);
    }

    @PatchMapping("activate/customer/{id}")
    public String activeCustomer(@PathVariable("id") Long id) {

        return customerService.activeCustomer(id);
    }

    @PatchMapping("activate/seller/{id}")
    public String activeSeller(@PathVariable("id") Long id) {

        return sellerService.activateSeller(id);
    }


}
