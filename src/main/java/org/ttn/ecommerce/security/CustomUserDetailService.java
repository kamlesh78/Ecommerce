package org.ttn.ecommerce.security;


import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.ttn.ecommerce.entities.Role;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
        return new User(user.getEmail(),user.getPassword(),mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles){
        return roles.stream().map(role->new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
    }


    /*  other*/
    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
    }

//    public int enableUser(String email) {
//        return userRepository.activateUserById(email);
//    }
//
//    public void disableUser(String email) {
//        userRepository.deactivateUserById(email);
//    }

    public void increaseFailedAttempts(Optional<UserEntity> user) {
        int newFailAttempts = user.get().getInvalidAttemptCount()+1;
        userRepository.updateInvalidAttemptCount(newFailAttempts, user.get().getEmail());
    }

    public void resetFailedAttempts(String email) {
        userRepository.updateInvalidAttemptCount(0, email);
    }

    public void lock(Optional<UserEntity> user) {
        user.get().setLocked(false);
        userRepository.save(user.get());
    }

}
