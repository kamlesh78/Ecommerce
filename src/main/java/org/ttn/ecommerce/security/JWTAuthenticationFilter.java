package org.ttn.ecommerce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.ttn.ecommerce.entities.Token;
import org.ttn.ecommerce.repository.TokenRepository.AccessTokenRepository;
import org.ttn.ecommerce.repository.TokenRepository.BlackListTokenRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    BlackListTokenRepository blackListTokenRepository;

    @Autowired
    AccessTokenRepository accessTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = getJWTFromRequest(request);
        System.out.println(token);

        Optional<Token> accessToken = accessTokenRepository.findByToken(token);
        if (accessToken.isPresent()){


            if (StringUtils.hasText(token) && jwtGenerator.validateToken(token)) {

                String username = jwtGenerator.getUsernameFromJWT(token);
                System.out.println(username);
                UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
                System.out.println("1:" + userDetails.getUsername());
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
    }
        filterChain.doFilter(request,response);
    }

    private String getJWTFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
         if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7,bearerToken.length());

        }
        return null;
    }
}
