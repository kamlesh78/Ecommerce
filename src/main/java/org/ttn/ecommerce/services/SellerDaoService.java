package org.ttn.ecommerce.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttn.ecommerce.dto.update.CustomerPasswordDto;
import org.ttn.ecommerce.dto.update.SellerPasswordDto;
import org.ttn.ecommerce.entities.Address;
import org.ttn.ecommerce.entities.Customer;
import org.ttn.ecommerce.entities.Seller;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.exception.AddressNotFoundException;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.AddressRepository;
import org.ttn.ecommerce.repository.CustomerRepository;
import org.ttn.ecommerce.repository.SellerRepository;
import org.ttn.ecommerce.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class SellerDaoService {

    @Autowired
    TokenService tokenService;

    @Autowired
    SellerRepository sellerRepository;


    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public String emailFromToken(HttpServletRequest request){
        String token = tokenService.getJWTFromRequest(request);
        String email = tokenService.getUsernameFromJWT(token);
        return email;
    }


    /*Deactivate Seller*/
    public String deActivateSeller(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("Seller with Id : "+id+" not found"));
        userRepository.deactivateUserById(id);
        return "Seller with id : "+id+" deactivated";
    }


    /*Seller's profile*/
    public MappingJacksonValue sellerProfile(String email){
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("Seller Not Found"));

        SimpleBeanPropertyFilter simpleBeanPropertyFilter=SimpleBeanPropertyFilter.filterOutAllExcept("firstName","lastName","email","companyContact","contact","image");
        FilterProvider filterProvider =new SimpleFilterProvider().addFilter("seller-filter",simpleBeanPropertyFilter);
        MappingJacksonValue mappingJacksonValue= new MappingJacksonValue(seller);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    public ResponseEntity<String> updateProfile(String email,Seller seller) {
        Seller sellerEntity =sellerRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("Seller Not Found"));
        if(seller.getEmail()!=null) sellerEntity.setEmail(seller.getEmail());
        if(seller.getCompanyContact()!=null) sellerEntity.setCompanyContact(seller.getCompanyContact());
        if(seller.getFirstName()!=null) sellerEntity.setFirstName(seller.getFirstName());
        if(seller.getMiddleName()!=null) sellerEntity.setMiddleName(seller.getMiddleName());
        if(seller.getLastName()!=null) sellerEntity.setLastName(seller.getLastName());
        sellerRepository.save(sellerEntity);
        return new ResponseEntity<>("Seller Profile Detail Updated!",HttpStatus.OK);
    }

    /* Add Seller Address */
    public ResponseEntity<?> insertSellerAddress(String email, Address address) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("Seller Not Found"));
        if(addressRepository.existsByUserId(userEntity.getId())>=1){
            addressRepository.deleteAddressById(userEntity.getId());
        }
        address.setUserEntity(userEntity);
        addressRepository.save(address);
        return new ResponseEntity<>("Address inserted",HttpStatus.OK);

    }


    /*display Seller addresses*/
    public String viewSellerAddresses(String email) throws IOException {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("Seller Not Found"));
        Set<Address> addresses = userEntity.getAddresses();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String output = mapper.writeValueAsString(addresses);
        return output;
    }

    /* Delete Address by Id */
    public String deleteSellerAddressById(String email, Long id) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User Not found"));
        Address address =   addressRepository.findById(id).orElseThrow( ()->new AddressNotFoundException("Address associated with Id :"+ id +" Not Found .  Please provide correct Id"));


        /*bonus feature */
        /* check if Address record belong to current customer or not */

        if(userEntity.getId() == address.getUserEntity().getId()){
            addressRepository.deleteAddressById(id);
            return "Address with id:" + id + " successfully deleted from database";
        }else{
            return "Address record associated with given ID provided do not belong to you! Please Proved correct address id";
        }

    }

    public String updateSellerAddressById(String email,Long id,Address address){
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User Not Found"));
        Address userAddress = addressRepository.findById(id).orElseThrow(()->new AddressNotFoundException("Address associated with Id : "+ id + " Not Found .  Please provide correct Id\""));
        address.setId(id);
        address.setUserEntity(userEntity);
        addressRepository.save(address);
        return "Address Updated";
    }


    /*Update Address*/
    public ResponseEntity<String> updateAddress(String email,Seller seller) {
        Seller sellerEntity =sellerRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("Customer Not Found"));
        if(seller.getEmail()!=null) sellerEntity.setEmail(seller.getEmail());
        if(seller.getCompanyContact()!=null) sellerEntity.setCompanyContact(seller.getCompanyContact());
        if(seller.getFirstName()!=null) sellerEntity.setFirstName(seller.getFirstName());
        if(seller.getMiddleName()!=null) sellerEntity.setMiddleName(seller.getMiddleName());
        if(seller.getLastName()!=null) sellerEntity.setLastName(seller.getLastName());
        sellerRepository.save(sellerEntity);
        return new ResponseEntity<>("Seller Profile Detail Updated!",HttpStatus.OK);
    }

    public ResponseEntity<String> updatePassword(SellerPasswordDto sellerPasswordDto, String email) {

        Seller seller = sellerRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("Seller Not Found"));
        seller.setPassword(passwordEncoder.encode(sellerPasswordDto.getPassword()));
        sellerRepository.save(seller);

        return new ResponseEntity<>("Password Updated",HttpStatus.OK);
    }

    public String activateSeller(Long id){
        UserEntity userEntity = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("Seller with Id : "+id+" not found"));
        userRepository.activateUserById(id);
        return "Seller with id : "+id+" not found";
    }
//
//    /*list all customer*/
//    public List<Customer> listAllSellers(){
//        //Pageable pageable =  PageRequest.of(0,10,Sort.by("email").ascending());
//    //    customerRepository.findAll(pageable);
//        List<Customer> customers=customerRepository.findAll();
//        customers.stream().forEach(System.out::println);
//        return customers;
//    }
//
//
    /*Deactivate Seller*/

}
