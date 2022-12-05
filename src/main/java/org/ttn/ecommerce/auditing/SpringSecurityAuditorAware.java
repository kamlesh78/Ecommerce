package org.ttn.ecommerce.auditing;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (loggedInUser == null) {
            username = "Anonymous User";
        } else {
            username = loggedInUser.getName();
        }

        return Optional.ofNullable(username);
    }
}