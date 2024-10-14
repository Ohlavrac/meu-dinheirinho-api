package com.ohlavrac.meu_dinheirinho_api.infra.security;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ohlavrac.meu_dinheirinho_api.domain.entities.users.UserDetailsImpl;
import com.ohlavrac.meu_dinheirinho_api.domain.entities.users.UsersEntity;
import com.ohlavrac.meu_dinheirinho_api.repositories.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = recoverTokenFromHeader(request);
        
        if (verifyEndpoint(request.getRequestURI())) {
            var login = tokenService.validateToken(token);
            
            if (login != null) {
                UsersEntity user = userRepository.findByEmail(login).orElseThrow(() -> new RuntimeException("User not found"));
                UserDetailsImpl userDetailsImpl = new UserDetailsImpl(user);

                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsImpl.getUsername(), null, userDetailsImpl.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new RuntimeException("Token erro");
            }
        }

        filterChain.doFilter(request, response);
    }


    private String recoverTokenFromHeader(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");

        if (authHeader == null) return null;

        return authHeader.replace("Beared", "");
    }

    private boolean verifyEndpoint(String uriRequest) {
        return Arrays.asList(SecurityConfig.ENDPOINTS_WITH_OUT_AUTH).contains(uriRequest);
    }
}
