package org.ttn.ecommerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.ttn.ecommerce.entities.Address;
import org.ttn.ecommerce.entities.Role;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.repository.RoleRepository;
import org.ttn.ecommerce.repository.UserRepository;
import org.ttn.ecommerce.validations.Password;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class bootstrap implements ApplicationRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(roleRepository.count());

        if(roleRepository.count()<1){
            Role admin =new Role();
            admin.setAuthority("ROLE_ADMIN");

            Role customer = new Role();
            customer.setAuthority("ROLE_CUSTOMER");

            Role seller = new Role();
            seller.setAuthority("ROLE_SELLER");

            roleRepository.save(admin);
            roleRepository.save(customer);
            roleRepository.save(seller);

        }
        if(userRepository.count()<1){

            Set<Address> addresses = new HashSet<>();
            Address address = new Address();
            address.setCity("Haldwani");
            address.setState("Uttarakhand");
            address.setCountry("India");
            address.setAddressLine("Talla Dewlchour");
            address.setZipCode("263139");

            UserEntity userEntity =new UserEntity();
            userEntity.setFirstName("admin");
            userEntity.setLastName("singh");
            userEntity.setActive(true);
            userEntity.setEmail("kamlesh.singh@tothenew.com");
            userEntity.setDeleted(false);
            userEntity.setExpired(false);
            userEntity.setLocked(false);
            userEntity.setInvalidAttemptCount(0);
            userEntity.setPassword(passwordEncoder.encode("admin123"));

            address.setUserEntity(userEntity);

            addresses.add(address);
            userEntity.setAddresses(addresses);

            Role roles = roleRepository.findByAuthority("ROLE_ADMIN").get();
            userEntity.setRoles(Collections.singletonList(roles));

            userRepository.save(userEntity);



        }
    }
}
