package org.ttn.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;
import org.ttn.ecommerce.controller.CustomerController;
import org.ttn.ecommerce.entities.Customer;
import org.ttn.ecommerce.entities.UserEntity;

import javax.servlet.http.HttpServletRequest;

@Service


public class CustomerDaoService {

    @Autowired
    TokenService tokenService;

    @Autowired
    CustomerRe

    public String emailFromToken(HttpServletRequest request){
        String token = tokenService.getJWTFromRequest(request);
        String email = tokenService.getUsernameFromJWT(token);
        return email;
    }
    public MappingJacksonValue customerProfile(String email){
        Customer customer = cus

    }
}
