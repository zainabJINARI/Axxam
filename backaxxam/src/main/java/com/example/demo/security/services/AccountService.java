package com.example.demo.security.services;

import java.util.List;
import com.example.demo.security.entities.AppRole;
import com.example.demo.security.entities.AppUser;

public interface AccountService {
	AppUser addNewUser(AppUser appUser);
	AppRole addNewRole(AppRole appRole);
	void addRoleToUser(String username,String role);
	AppUser loadUserByUsername(String username); 
	List<AppUser> listUsers();
	void logout();
	AppUser updateUser( AppUser updatedUser);

}
