package com.example.demo.security;

import java.util.ArrayList;
import java.util.Collection;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.demo.security.entities.AppUser;
import com.example.demo.security.services.AccountService;
import com.nimbusds.jose.jwk.source.ImmutableSecret;




@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)


public class SecurityConfig {

    private final AccountService accountService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Define your JWT secret key (you can store this in application.properties)
    private static final String SECRET_KEY = "jK8%7fT1@qweRtYzUoPlZxAbCD345^&*mnp0vGhX12WY89KLMNs5eFd\r\n";

    public SecurityConfig(AccountService accountService) {
        this.accountService = accountService;
    }

    
	 
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
        	  System.out.println("am here "+username);
            AppUser appUser = accountService.loadUserByUsername(username);
         
            if (appUser == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            appUser.getAppRoles().forEach(r -> {
                authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
                System.out.println("roles");
                System.out.println(new SimpleGrantedAuthority(r.getRoleName()));
            });

            UserDetails us = User.builder()
                    .username(appUser.getUsername())
                    .password(appUser.getPassword())
                    .authorities(authorities)
                    .build();
            
            
            return us;
        };
    }
    
//    @Bean 
//	PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }

   
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(ar -> ar
                        .requestMatchers("/auth/*").permitAll()
                        .anyRequest().authenticated() 
                )
                .oauth2ResourceServer(oa -> oa.jwt(Customizer.withDefaults())) 
                .build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(SECRET_KEY.getBytes()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS256).build();
    }

    

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return new ProviderManager(daoAuthenticationProvider);
    }
    
   
}
