package org.ttn.ecommerce.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttn.ecommerce.controller.CustomerController;
import org.ttn.ecommerce.dto.update.CustomerPasswordDto;
import org.ttn.ecommerce.entities.Address;
import org.ttn.ecommerce.entities.Customer;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.exception.AddressNotFoundException;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.AddressRepository;
import org.ttn.ecommerce.repository.CustomerRepository;
import org.ttn.ecommerce.repository.SellerRepository;
import org.ttn.ecommerce.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class CustomerDaoService {

    @Autowired
    TokenService tokenService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;


    public String emailFromToken(HttpServletRequest request){
        String token = tokenService.getJWTFromRequest(request);
        String email = tokenService.getUsernameFromJWT(token);
        return email;
    }


    /*          list all customer           */
    public MappingJacksonValue listAllCustomers() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        List<Customer> customers = customerRepository.findAll();

        FilterProvider filters = new SimpleFilterProvider() .addFilter(
                "customerFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id","firstName","lastName","email","isActive"));
//
//        String jsonString = mapper.writer(filters)
//                .withDefaultPrettyPrinter()
//                .writeValueAsString(customers);
//        return jsonString;
        MappingJacksonValue mappingJacksonValue =new MappingJacksonValue(customers);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;

    }

    /*      Customer Profile        */
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

    /*      Add customer address        */
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


    /*      display customer addresses      */
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


        /*      Bonus feature       */
        /* check if Address record belong to current customer or not */

        if(userEntity.getId() == address.getUserEntity().getId()){
            addressRepository.deleteAddressById(id);
            return "Address with id:" + id + " successfully deleted from database";
        }else{
            return "Address record associated with given ID provided do not belong to you!\n Please Proved correct address id";
        }
    }

    /*       Update Address        */
    public String updateCustomerAddressById(String email,Long id,Address address){
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User Not Found"));
        Address userAddress = addressRepository.findById(id).orElseThrow(()->new AddressNotFoundException("Address Not Found"));
        address.setId(id);
        address.setUserEntity(userEntity);
        addressRepository.save(address);
        return "Address Updated";
    }

    /*    Update Profile      */
    public ResponseEntity<String> updateProfile(String email,Customer customer) {
        Customer customerEntity =customerRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("Customer Not Found"));
        if(customer.getEmail()!=null) customerEntity.setEmail(customer.getEmail());
        if(customer.getContact()!=null) customerEntity.setContact(customer.getContact());
        if(customer.getFirstName()!=null) customerEntity.setFirstName(customer.getFirstName());
        if(customer.getMiddleName()!=null) customerEntity.setMiddleName(customer.getMiddleName());
        if(customer.getLastName()!=null) customerEntity.setLastName(customer.getLastName());
        customerRepository.save(customerEntity);
        return new ResponseEntity<>("Customer Profile Detail Updated!",HttpStatus.OK);
    }

    /*      Update Password         */
    public ResponseEntity<String> updatePassword(CustomerPasswordDto customerPasswordDto, String email) {

        Customer customer = customerRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("Customer Not Found"));

        /*bonus feature :           update using patch using custom query*/

        customerRepository.updatePassword(passwordEncoder.encode(customerPasswordDto.getPassword()),customer.getId());

        return new ResponseEntity<>("Password Updated",HttpStatus.OK);
    }


    /*       Deactivate Customer          */
    public String deActiveCustomer(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("Customer with Id : "+id+" not found"));
        if(userEntity.isActive()){
            userRepository.deactivateUserById(id);
            emailService.setSubject("Account Deactivated");
            emailService.setMessage(userEntity.getFirstName() + " your account has been deactivated.\n Please contact admin to activate your account now");
            emailService.setToEmail(userEntity.getEmail());
            emailService.sendEmail();
            /*Exception handling for mail*/

        }
        userRepository.deactivateUserById(id);
        return "Customer with id : "+id+" deactivated";
    }

    /*   Activate Customer   */
    public String activeCustomer(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("Customer with Id : "+id+" not found"));
        if(!userEntity.isActive()){
            userRepository.activateUserById(id);
            emailService.setSubject("Account Activated");
            emailService.setMessage(userEntity.getFirstName() + " your account has been activated.\n You can access your account now");
            emailService.setToEmail(userEntity.getEmail());
            emailService.sendEmail();
            /*Exception handling for mail*/

        }
        return "Customer with id : "+id+" not found";
    }
}
