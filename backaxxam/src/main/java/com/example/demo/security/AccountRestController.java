package com.example.demo.security;

import java.util.List;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.security.entities.AppRole;
import com.example.demo.security.entities.AppUser;
import com.example.demo.security.model.SignUpRequest;
import com.example.demo.security.repository.AppRoleRepository;
import com.example.demo.security.services.AccountService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.web.bind.annotation.*;





@RestController
@CrossOrigin()
public class AccountRestController {

	private final AuthenticationManager authenticationManager;
	private final JwtEncoder jwtEncoder;
	private AccountService accountService;
	private AppRoleRepository appRoleRepository;
	private final PasswordEncoder passwordEncoder;

	public AccountRestController(AccountService accountService, AuthenticationManager authenticationManager,
			JwtEncoder jwtEncoder, AppRoleRepository appRoleRepository  , PasswordEncoder passwordEncoder
) {
		super();
		this.accountService = accountService;
		this.authenticationManager = authenticationManager;
		this.jwtEncoder = jwtEncoder;
		this.appRoleRepository = appRoleRepository;
		this.passwordEncoder = passwordEncoder ;
	}

	@GetMapping("/users")
	public List<AppUser> appUsers() {
		return accountService.listUsers();
	}

	@PostMapping("/auth/login")
	public Map<String, Object> login(@RequestParam String username, @RequestParam String password) {
		Authentication authentication = null;
		System.out.println("sysout");
		try {
			authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			System.out.println("Authentication successful: " + authentication);
		} catch (Exception e) {
			System.out.println("Authentication failed: " + e);
			throw new RuntimeException("Invalid username or password");
		}

		Instant now = Instant.now();
		String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));

		JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder().issuedAt(now).expiresAt(now.plus(1, ChronoUnit.HOURS))
				.subject(authentication.getName()).claim("scope", scope).build();

		System.out.println("JWT Claims: " + jwtClaimsSet.getClaims());

		// Create JWT Encoder parameters
		JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
				.from(JwsHeader.with(MacAlgorithm.HS256).build(), jwtClaimsSet);
		String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();

		// Fetch the authenticated user's details
		String usernameAuthenticated = authentication.getName();
		List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		// Return token, username, and roles
		return Map.of("access-token", jwt, "username", usernameAuthenticated, "roles", roles);
	}

	@PostMapping("/auth/logout")
	public Map<String, String> logout() {
		SecurityContextHolder.clearContext();
		return Map.of("message", "Logout successful");
	}

	@PostMapping("/auth/signup")
	public AppUser signup(@RequestBody SignUpRequest signUpRequest) {
		AppRole role = appRoleRepository.findByRoleName(signUpRequest.getRole());
		AppUser appUser = AppUser.builder().username(signUpRequest.getUsername()).password(signUpRequest.getPassword())
				.email(signUpRequest.getEmail()).appRoles(List.of(role)).build();
		return accountService.addNewUser(appUser);
	}

	
	@GetMapping("/auth/user/{username}")
	public Map<String, String> getUserByUsername(@PathVariable String username) {
		AppUser appUser = accountService.loadUserByUsername(username);
		if (appUser == null) {
			throw new RuntimeException("User not found with username: " + username);
		}
		return Map.of("email", appUser.getEmail(), "username", appUser.getUsername());
	}

	@PostMapping("/auth/update/{username}")
	public AppUser updateUser(@PathVariable String username, @RequestBody SignUpRequest signUpRequest) {

		AppUser existingUser = accountService.loadUserByUsername(username);
	    if (existingUser == null) {
	        throw new RuntimeException("User not found with username: " + username);
	    }

	    if (signUpRequest.getEmail() != null && !signUpRequest.getEmail().equals(existingUser.getEmail())) {
	        existingUser.setEmail(signUpRequest.getEmail());
	    }

	    if (signUpRequest.getPassword() != null && !signUpRequest.getPassword().equals(existingUser.getPassword())) {
	    	String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
	        existingUser.setPassword(encodedPassword);

	    }

	    return accountService.updateUser(existingUser);
	}


}
