package org.ttn.ecommerce.controller;


import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Customer> listAllCustomer(){
        return customerDaoService.listAllCustomers();

    }


    @PutMapping("deactive/customer/{id}")
    public String deActiveCustomer(@PathVariable("id") Long id){
        return customerDaoService.deactiveCustomer(id);
    }


    @PutMapping("deactive/seller/{id}")
    public String deActiveSeller(@PathVariable("id") Long id){
        return sellerDaoService.deactivateSeller(id);
    }
}
