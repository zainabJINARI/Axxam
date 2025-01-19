package com.example.demo.security.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.example.demo.security.entities.AppUser;
import com.example.demo.security.repository.AppUserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final AppUserRepository appUserRepository;

    public AppUser getCurrentUser() throws AuthenticationException {
        // Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
            throw new AuthenticationException("User is not authenticated or JWT is missing.") {};
        }

        // Extract JWT from the principal
        Jwt jwt = (Jwt) authentication.getPrincipal();

        // Get the username (or whatever identifier is used) from the JWT
        String username = jwt.getClaimAsString("sub"); // "sub" is commonly used for username, but adjust if needed

        if (username == null || username.isEmpty()) {
            throw new AuthenticationException("JWT does not contain a valid 'sub' claim for the username.") {};
        }

        // Fetch the user from the database using the username
        AppUser appUser = appUserRepository.findByUsername(username);
        
        if (appUser == null) {
            throw new AuthenticationException("User with username '" + username + "' not found.") {};
        }

        return appUser;
    }
}
