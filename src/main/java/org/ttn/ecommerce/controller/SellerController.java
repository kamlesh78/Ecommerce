package org.ttn.ecommerce.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/seller")
public class SellerController {

    @PreAuthorize("hasRole('ROLE_SELLER')")
    @GetMapping("login")
    public String getData(){
        return "a";
    }
}
