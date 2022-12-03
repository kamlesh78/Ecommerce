package org.ttn.ecommerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.ttn.ecommerce.entity.user.Address;
import org.ttn.ecommerce.entity.user.Role;
import org.ttn.ecommerce.entity.user.UserEntity;
import org.ttn.ecommerce.repository.UserRepository.RoleRepository;
import org.ttn.ecommerce.repository.UserRepository.UserRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@Transactional
public class bootstrap implements ApplicationRunner {


    private RoleRepository roleRepository;


    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public bootstrap(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
            System.out.println(roles.getId());
            userEntity.setRoles(Collections.singletonList(roles));

            userRepository.save(userEntity);



        }
    }
}
