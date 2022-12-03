package org.ttn.ecommerce.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.ttn.ecommerce.entity.UserEntity;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    CustomUserDetailService customUserDetailService;
    @Autowired
    UserRepository userRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String email = request.getParameter("email");
        UserEntity user = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User Not Found"));
        if (user != null) {
            if (user.isActive() && !user.isLocked()) {
                if (user.getInvalidAttemptCount() < SecurityConstants.MAX_LOGIN_ATTEMPT - 1) {
                    customUserDetailService.increaseFailedAttempts(Optional.of(user));
                } else {
                    customUserDetailService.lock(Optional.of(user));
                    exception = new LockedException("Your account has been locked due to 3 failed attempts."
                            + " Contact Admin to remove lock on your account.");
                }
            }
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", exception.getMessage());
        body.put("path", request.getServletPath());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}