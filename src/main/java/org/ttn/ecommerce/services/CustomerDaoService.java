package org.ttn.ecommerce.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttn.ecommerce.controller.CustomerController;
import org.ttn.ecommerce.entities.Address;
import org.ttn.ecommerce.entities.Customer;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.exception.AddressNotFoundException;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.AddressRepository;
import org.ttn.ecommerce.repository.CustomerRepository;
import org.ttn.ecommerce.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Service
@Transactional
public class CustomerDaoService {

    @Autowired
    TokenService tokenService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;


    public String emailFromToken(HttpServletRequest request){
        String token = tokenService.getJWTFromRequest(request);
        String email = tokenService.getUsernameFromJWT(token);
        return email;
    }
    public MappingJacksonValue customerProfile(String email){
        Optional<Customer> customer = customerRepository.findByEmail(email);
        System.out.println(customer.get().getImages());
//        if(customer.isPresent()){
            SimpleBeanPropertyFilter simpleBeanPropertyFilter=SimpleBeanPropertyFilter.filterOutAllExcept("firstName","lastName","email","isActive","contact","image");
            FilterProvider filterProvider =new SimpleFilterProvider().addFilter("customerFilter",simpleBeanPropertyFilter);
            MappingJacksonValue mappingJacksonValue= new MappingJacksonValue(customer);
            mappingJacksonValue.setFilters(filterProvider);
            return mappingJacksonValue;

//        }else{
//        }

    }

    /*add customer address*/
    public ResponseEntity<?> insertCustomerAddress(String email, Address address) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if(userEntity.isPresent()){
            address.setUserEntity(userEntity.get());
            addressRepository.save(address);
            return new ResponseEntity<>("Address inserted",HttpStatus.OK);
        }else{
            /* exception*/
            return new ResponseEntity<>("user not found", HttpStatus.BAD_REQUEST);
        }
    }


    /*display customer addresses*/
    public MappingJacksonValue viewCustomerAddresses(String email) throws IOException {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
//        if(userEntity.isPresent()){
                        SimpleBeanPropertyFilter simpleBeanPropertyFilter=SimpleBeanPropertyFilter.filterOutAllExcept("addresses");
            FilterProvider filterProvider = new SimpleFilterProvider().addFilter("customerFilter",simpleBeanPropertyFilter);
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(userEntity);
            mappingJacksonValue.setFilters(filterProvider);
        System.out.println("hh");
            return mappingJacksonValue;
//              }
    }

    public String deleteCustomerAddressById(String email, Long id) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User Not found"));
        Address address =   addressRepository.findById(id).orElseThrow( ()->new AddressNotFoundException("Address not Found"));


        /*bonus feature */
        /* check if Address record belong to current customer or not */

        if(userEntity.getId() == address.getUserEntity().getId()){
            addressRepository.deleteAddressById(id);
            return "Address with id:" + id + " successfully deleted from database";
        }else{
            return "Address record associated with given ID provided do not belong to you!\n Please Proved correct address id";
        }

    }

    public String updateCustomerAddressById(String email,Long id,Address address){
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User Not Found"));
        Address userAddress = addressRepository.findById(id).orElseThrow(()->new AddressNotFoundException("Address Not Found"));
        address.setId(id);
        address.setUserEntity(userEntity);
        addressRepository.save(address);
        return "Address Updated";
    }
}
