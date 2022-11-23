package org.ttn.ecommerce.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.ttn.ecommerce.entities.Customer;
import org.ttn.ecommerce.entities.Seller;
import org.ttn.ecommerce.repository.CustomerRepository;
import org.ttn.ecommerce.repository.SellerRepository;
import org.ttn.ecommerce.services.CustomerDaoService;
import org.ttn.ecommerce.services.SellerDaoService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
/*check
where we used
Customer or Seller as parameter to
check custom JsonFilter
 */
    @Autowired
    private CustomerDaoService customerDaoService;

    @Autowired
    private SellerDaoService sellerDaoService;



    @GetMapping("list/customers")
    public MappingJacksonValue listAllCustomer() throws JsonProcessingException {
        return customerDaoService.listAllCustomers();

    }

    @GetMapping("list/sellers")
    public MappingJacksonValue listAllSeller() throws JsonProcessingException {
        return sellerDaoService.listAllSellers();

    }

    @PatchMapping("deactivate/customer/{id}")
    public String deActiveCustomer(@PathVariable("id") Long id){
        return customerDaoService.deActiveCustomer(id);
    }

    @PatchMapping("deactivate/seller/{id}")
    public String deActiveSeller(@PathVariable("id") Long id){
        return sellerDaoService.deActivateSeller(id);
    }

    @PatchMapping("activate/customer/{id}")
    public String activeCustomer(@PathVariable("id") Long id){
        return customerDaoService.activeCustomer(id);
    }

    @PatchMapping("activate/seller/{id}")
    public String activeSeller(@PathVariable("id") Long id){
        return sellerDaoService.activateSeller(id);
    }

}
