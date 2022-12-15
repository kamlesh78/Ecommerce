package org.ttn.ecommerce.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.ttn.ecommerce.entity.user.UserEntity;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.UserRepository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomUserDetailService customUserDetailService;



    private ObjectMapper objectMapper = new ObjectMapper();

    private final AuthenticationManager authenticationManager;




    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        String email = request.getParameter("email");
        UserEntity user = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User Not Found"));
        if (user != null) {
            if (user.isActive() && !user.isLocked()) {
                if (user.getInvalidAttemptCount() < SecurityConstants.MAX_LOGIN_ATTEMPT - 1) {
                    customUserDetailService.increaseFailedAttempts(Optional.of(user));
                } else {
                    customUserDetailService.lock(Optional.of(user));
                    failed = new LockedException("Your account has been locked due to 3 failed attempts."
                            + " Contact Admin to remove lock on your account.");
                }
            }
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", failed.getMessage());
        body.put("path", request.getServletPath());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}