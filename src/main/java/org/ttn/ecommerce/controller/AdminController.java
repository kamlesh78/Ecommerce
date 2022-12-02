package org.ttn.ecommerce.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.ttn.ecommerce.services.CustomerServiceImpl;
import org.ttn.ecommerce.services.SellerServiceImpl;
import org.ttn.ecommerce.services.product.ProductService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    /*check
    where we used
    Customer or Seller as parameter to
    check custom JsonFilter
     */
    @Autowired
    private CustomerServiceImpl customerDaoService;

    @Autowired
    private SellerServiceImpl sellerDaoService;

    @Autowired
    private  ProductService productService;
    /*  List Seller's address, User can provide Custom  pagination and sorting fields */
    @GetMapping("list/customers")
    public MappingJacksonValue listAllCustomer(
            @RequestParam(name = "pageSize", required = false) String pageSize,
            @RequestParam(name = "pageOffset", required = false) String pageOffset,
            @RequestParam(name = "sort", required = false) String sortBy
    ) throws JsonProcessingException {

        String customPageSize = pageSize == null ? "10" : pageSize;
        String customPageOffset = pageOffset == null ? "0" : pageOffset;
        String customSortBy = sortBy == null ? "email" : sortBy;
        return customerDaoService.listAllCustomers(customPageSize, customPageOffset, customSortBy);

    }


    /* List Seller's address
     *  User can provide Custom pagination and sorting fields
     */

    @GetMapping("list/sellers")
    public MappingJacksonValue listAllSeller(
            @RequestParam(name = "pageSize", required = false) String pageSize,
            @RequestParam(name = "pageOffset", required = false) String pageOffset,
            @RequestParam(name = "sort", required = false) String sortBy) throws JsonProcessingException {


        /**
         *         Default Values if Custom Pagination and Sorting fields not provided
         */
        String customPageSize = pageSize == null ? "10" : pageSize;
        String customPageOffset = pageOffset == null ? "0" : pageOffset;
        String customSortBy = sortBy == null ? "email" : sortBy;
        return sellerDaoService.listAllSellers(customPageSize, customPageOffset, customSortBy);
    }

    @PatchMapping("deactivate/customer/{id}")
    public String deActiveCustomer(@PathVariable("id") Long id) {

        return customerDaoService.deActiveCustomer(id);
    }

    @PatchMapping("deactivate/seller/{id}")
    public String deActiveSeller(@PathVariable("id") Long id) {

        return sellerDaoService.deActivateSeller(id);
    }

    @PatchMapping("activate/customer/{id}")
    public String activeCustomer(@PathVariable("id") Long id) {

        return customerDaoService.activeCustomer(id);
    }

    @PatchMapping("activate/seller/{id}")
    public String activeSeller(@PathVariable("id") Long id) {

        return sellerDaoService.activateSeller(id);
    }


    /**
     *      Activate Product Created By Seller
     */

    @PutMapping("activate/product/{productId}")
    public String activateProduct(@PathVariable("productId") long productId){

        return productService.activateProduct(productId);
    }

    /**
     *      DeActivate Product
     */
    @PutMapping("deactivate/product/{productId}")
    public String deactivateProduct(@Param("productId") Long id){

        return productService.deactivateProduct(id);
    }
}
