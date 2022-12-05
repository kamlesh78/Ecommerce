package org.ttn.ecommerce.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.ttn.ecommerce.dto.responseDto.userDto.CustomerResponseDto;
import org.ttn.ecommerce.dto.responseDto.userDto.SellerResponseDto;
import org.ttn.ecommerce.services.CustomerService;
import org.ttn.ecommerce.services.ProductService;
import org.ttn.ecommerce.services.SellerService;

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


    /**
     * @param pageSize
     * @param pageOffset
     * @param sortBy
     * @return
     * @throws JsonProcessingException
     * @Problem List Seller's address, User can provide Custom  pagination and sorting fields
     */
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


    /**
     * @param pageSize
     * @param pageOffset
     * @param sortBy
     * @return
     * @throws JsonProcessingException
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

    /**
     * @param id
     * @return
     */
    @PatchMapping("deactivate/customer/{id}")
    public String deActiveCustomer(@PathVariable("id") Long id) {

        return customerService.deActiveCustomer(id);
    }

    /**
     * @param id
     * @return
     */
    @PatchMapping("deactivate/seller/{id}")
    public String deActiveSeller(@PathVariable("id") Long id) {

        return sellerService.deActivateSeller(id);
    }

    /**
     * @param id
     * @return
     */
    @PatchMapping("activate/customer/{id}")
    public String activeCustomer(@PathVariable("id") Long id) {

        return customerService.activeCustomer(id);
    }

    /**
     * @param id
     * @return
     */
    @PatchMapping("activate/seller/{id}")
    public String activeSeller(@PathVariable("id") Long id) {

        return sellerService.activateSeller(id);
    }


}
