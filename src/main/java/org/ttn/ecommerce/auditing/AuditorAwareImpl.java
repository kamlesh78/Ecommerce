package org.ttn.ecommerce.auditing;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.ttn.ecommerce.security.CustomUserDetailService;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication auth =SecurityContextHolder.getContext().getAuthentication();
        if (auth == null ) {
            return Optional.empty();
        }

        String currentUser =  auth.getPrincipal().toString();
        return Optional.ofNullable(currentUser);
    }
}