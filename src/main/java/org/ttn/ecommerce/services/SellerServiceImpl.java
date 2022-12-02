package org.ttn.ecommerce.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttn.ecommerce.dto.responseDto.userDto.SellerResponseDto;
import org.ttn.ecommerce.dto.update.SellerPasswordDto;
import org.ttn.ecommerce.entities.Address;
import org.ttn.ecommerce.entities.Seller;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.exception.AddressNotFoundException;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.AddressRepository;
import org.ttn.ecommerce.repository.SellerRepository;
import org.ttn.ecommerce.repository.UserRepository;
import org.ttn.ecommerce.services.tokenService.TokenService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class SellerServiceImpl{

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

    @Autowired
    EmailService emailService;

    @Autowired
    EmailServicetry emailServicetry;



    public String emailFromToken(HttpServletRequest request){
        String token = tokenService.getJWTFromRequest(request);
        String email = tokenService.getUsernameFromJWT(token);
        return email;
    }


    /*Deactivate Seller*/

    public String deActivateSeller(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("Seller with Id : "+id+" not found"));
        if(userEntity.isActive()){
            userRepository.deactivateUserById(id);

            /*      Sending DeActivation Mail To Seller       */
        try {


            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject("Account Deactivated");
            simpleMailMessage.setText(userEntity.getFirstName() + " your account has been deactivated.\n Please contact admin to activate your account now");
            simpleMailMessage.setTo(userEntity.getEmail());

            emailServicetry.sendEmail(simpleMailMessage);
        }catch (MailException ex){
            return "Cant Send Mail || Mailing server is down || Kindly wait";
        }
            /*Exception handling for mail*/

        }
        userRepository.deactivateUserById(id);
        return "Seller with id : "+id+" deactivated";
    }

    /* Activate Seller */

    public String activateSeller(Long id){
        UserEntity userEntity = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("Seller with Id : "+id+" not found"));
        if(!userEntity.isActive()){
            userRepository.activateUserById(id);

            /* Sending Mail To Seller*/
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject("Account Activated");
            simpleMailMessage.setText(userEntity.getFirstName() + " your account has been activated.\n You can access your account now");
            simpleMailMessage.setTo(userEntity.getEmail());

            emailServicetry.sendEmail(simpleMailMessage);
            /*Exception handling for mail*/

        }
        userRepository.activateUserById(id);
        return "Seller with id : "+id+" activated";
    }

    /*Seller's profile*/

    public SellerResponseDto sellerProfile(String email){
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("Seller Not Found"));

        SellerResponseDto sellerResponseDto = new SellerResponseDto();
        sellerResponseDto.setId(seller.getId());
        sellerResponseDto.setFirstName(seller.getFirstName());
        sellerResponseDto.setLastName(seller.getLastName());
        sellerResponseDto.setCompanyName(seller.getCompanyName());
        sellerResponseDto.setCompanyContact(seller.getCompanyContact());
        sellerResponseDto.setGst(seller.getGst());
        sellerResponseDto.setAddress(seller.getAddresses());

        return sellerResponseDto;

    }

    /* Update Profile */

    public ResponseEntity<String> updateProfile(String email, Seller seller) {
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

    /* Update Seller Address */

    public String updateSellerAddressById(String email, Long id, Address address){

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User Not Found"));
        Address userAddress = addressRepository.findById(id).orElseThrow(()->new AddressNotFoundException("Address associated with Id : "+ id + " Not Found .  Please provide correct Id\""));

        if(address.getCity()!=null) userAddress.setCity(address.getCity());
        if(address.getState()!=null) userAddress.setState(address.getState());
        if(address.getCountry()!=null) userAddress.setCountry(address.getCountry());
        if(address.getAddressLine()!=null) userAddress.setAddressLine(address.getAddressLine());
        if(address.getLabel()!=null) userAddress.setLabel(address.getLabel());
        if(address.getZipCode()!=null) userAddress.setZipCode(address.getZipCode());

        addressRepository.save(userAddress);
        return "Address Updated";
    }


    /*Update Address*/

    public ResponseEntity<String> updateAddress(String email, Seller seller) {
        Seller sellerEntity =sellerRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("Customer Not Found"));
        if(seller.getEmail()!=null || seller.getEmail()!="") sellerEntity.setEmail(seller.getEmail());
        if(seller.getCompanyContact()!=null || seller.getCompanyContact()!="") sellerEntity.setCompanyContact(seller.getCompanyContact());
        if(seller.getFirstName()!=null || seller.getFirstName()!="") sellerEntity.setFirstName(seller.getFirstName());
        if(seller.getMiddleName()!=null || seller.getMiddleName()!="") sellerEntity.setMiddleName(seller.getMiddleName());
        if(seller.getLastName()!=null || seller.getLastName()!="") sellerEntity.setLastName(seller.getLastName());
        sellerRepository.save(sellerEntity);
        return new ResponseEntity<>("Seller Profile Detail Updated!",HttpStatus.OK);
    }


    /* Update Sellers Password */

    public ResponseEntity<String> updatePassword(SellerPasswordDto sellerPasswordDto, String email) {

        Seller seller = sellerRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("Seller Not Found"));

        sellerRepository.updatePassword(passwordEncoder.encode(sellerPasswordDto.getPassword()),seller.getId());

        /*      Sending Email to USer for password reset Alert      */
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(seller.getEmail());
        simpleMailMessage.setSubject("Password Updated");
        simpleMailMessage.setText(seller.getFirstName() + " password for your account has updated at : " + LocalDateTime.now() + "\nPlease Contact Admin if it was not done by you");

        return new ResponseEntity<>("Password Updated",HttpStatus.OK);
    }

    /**
     *                      List all Sellers
     */


    public List<SellerResponseDto> listAllSellers(String pageSize, String pageOffset, String sortBy){

        Pageable pageable = PageRequest.of(Integer.parseInt(pageOffset),Integer.parseInt(pageSize), Sort.by(new Sort.Order(
                Sort.Direction.DESC,sortBy)));

        Page<Seller> pages = sellerRepository.findAll(pageable);
       List<Seller> sellerList = pages.getContent();
       List<SellerResponseDto> responseDtoList = new ArrayList<>();

       for(Seller seller : sellerList){

           SellerResponseDto sellerResponseDto = new SellerResponseDto();
           sellerResponseDto.setId(seller.getId());
           sellerResponseDto.setFirstName(seller.getFirstName());
           sellerResponseDto.setLastName(seller.getLastName());
           sellerResponseDto.setCompanyContact(seller.getCompanyContact());
           sellerResponseDto.setCompanyName(seller.getCompanyName());
           sellerResponseDto.setGst(seller.getGst());
           sellerResponseDto.setActive(seller.isActive());
           sellerResponseDto.setAddress(seller.getAddresses());
           responseDtoList.add(sellerResponseDto);
       }

        return responseDtoList;
    }



}
