package com.example.demo.security.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.security.entities.AppRole;
import com.example.demo.security.entities.AppUser;
import com.example.demo.security.repository.AppRoleRepository;
import com.example.demo.security.repository.AppUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;



@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	
	private AppUserRepository appUserRepository;
	private AppRoleRepository appRoleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
    private HttpServletRequest request;
    
    @Autowired
    private HttpServletResponse response;

	public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository) {
		super();
		this.appUserRepository = appUserRepository;
		this.appRoleRepository = appRoleRepository;
		
	}
	
	// Méthode de logout
	@Override
    public void logout() {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    }
    
    
    

	@Override
	public AppUser addNewUser(AppUser appUser) {
		
	    // Vérifier si un utilisateur avec le même email existe déjà
	    if (appUserRepository.findByEmail(appUser.getEmail())  != null) {
	        throw new RuntimeException("Un utilisateur existe déjà avec cet email.");
	    }

	    // Crypter le mot de passe avant de sauvegarder
	    String pw = appUser.getPassword();
	    appUser.setPassword(passwordEncoder.encode(pw));
	    
	    // Sauvegarder le nouvel utilisateur
	    return appUserRepository.save(appUser);
	}

	
	

	@Override
	public AppRole addNewRole(AppRole appRole) {
		return appRoleRepository.save(appRole);
	}

	@Override
	public void addRoleToUser(String username, String role) {
		AppUser appUser = appUserRepository.findByUsername(username);
		AppRole appRole = appRoleRepository.findByRoleName(role);
		appUser.getAppRoles().add(appRole);
		
		
	}

	@Override
	public AppUser loadUserByUsername(String username) {		
		return appUserRepository.findByUsername(username);
	}

	@Override
	public List<AppUser> listUsers() {
		return appUserRepository.findAll();
	}
	
	
	
	@Override
	public AppUser updateUser(AppUser updatedUser) {
	    AppUser existingUser = appUserRepository.findByUsername(updatedUser.getUsername());
	    
	    if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(existingUser.getEmail())) {
	        existingUser.setEmail(updatedUser.getEmail());
	    }
	    
	    if (updatedUser.getPassword() != null && !updatedUser.getPassword().equals(existingUser.getPassword())) {
	        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
	    }
	    
	    return appUserRepository.save(existingUser);
	}


	
	
	
	
	


	




}
