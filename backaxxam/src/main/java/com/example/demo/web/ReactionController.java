package com.example.demo.web;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.PaginatedResponse;
import com.example.demo.dtos.ReactionDto;
import com.example.demo.entities.Reaction;
import com.example.demo.exceptions.AnnouncementNotFoundException;
import com.example.demo.repositories.ReactionRepo;
import com.example.demo.security.entities.AppUser;
import com.example.demo.security.services.AccountService;
import com.example.demo.services.IServiceReaction;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@CrossOrigin
public class ReactionController {
	
	private IServiceReaction serviceReaction;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private ReactionRepo reactionRepo;
	
	
	
	@GetMapping("/reactions/ann/{id}")
	public PaginatedResponse<ReactionDto> getAllReactionsByAnn(
			  @RequestParam(name = "page", defaultValue = "0") int page,
		        @RequestParam(name = "size", defaultValue = "5") int size,
		        @PathVariable String id
			){
				return serviceReaction.getAllReactionsByAnn(page,size,id);
		
	}
	
	@GetMapping("/reactions/user/{customerId}")
	public PaginatedResponse<ReactionDto> getAllReactionsByUser(
			 @RequestParam(name = "page", defaultValue = "0") int page,
		        @RequestParam(name = "size", defaultValue = "5") int size,
		        @PathVariable Long customerId
			){
		return serviceReaction.getAllReactionsByUser(page,size,customerId);
	}
	
	@PreAuthorize("hasAnyAuthority('SCOPE_ROLE_HOST')")
	@GetMapping("/reactions/unread")
	public PaginatedResponse<ReactionDto> getAllUnreadReactionsByHost(
			 @RequestParam(name = "page", defaultValue = "0") int page,
		        @RequestParam(name = "size", defaultValue = "5") int size
			){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
	    // Log the username
	    System.out.println("Username: " + username); 
		// Call your account service to fetch the user by username
	    AppUser user = accountService.loadUserByUsername(username); 
		
		return serviceReaction.getAllUnreadReactionsByHost(page,size,user.getId());
	}
	
	
	@PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER')")
	@PostMapping("/reactions")
	public Long createReaction(ReactionDto reaction) throws Exception {
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
	    // Log the username
	    System.out.println("Username: " + username); 
		// Call your account service to fetch the user by username
	    AppUser user = accountService.loadUserByUsername(username); 
		reaction.setCustomerId(user.getId());
		return serviceReaction.createReaction(reaction);
		
	}
	
	@PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN')")
	@DeleteMapping("/reactions/{id}")
	public void deleteReaction(@PathVariable Long id) {
		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		// Check if the user has the "ROLE_USER" role
		boolean hasUserRole = authorities.stream()
		        .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));	
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
	    // Log the username
	    System.out.println("Username: " + username); 
		// Call your account service to fetch the user by username
	    AppUser user = accountService.loadUserByUsername(username); 
		// Log or perform actions based on whether the user has the role
		if (hasUserRole) {
			
			if(reactionRepo.findById(id).get().getCustomerId()==user.getId()) {
				 serviceReaction.deleteReaction(id);	
			}	   
		} else {
			 serviceReaction.deleteReaction(id);
		}
		
	}
	
	@PreAuthorize("hasAnyAuthority('SCOPE_ROLE_HOST')")
	@PutMapping("/reactions/read/{id}")
	public boolean updateReactionStatus(@PathVariable Long id) throws AnnouncementNotFoundException {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
	    // Log the username
	    System.out.println("Username: " + username); 
		// Call your account service to fetch the user by username
	    AppUser user = accountService.loadUserByUsername(username); 
	    
	    return serviceReaction.updateReactionStatus(id, user.getId());
	    
	    
		
	}
	
	
	@PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN')")
	@PutMapping("/reactions")
	public ReactionDto updateReaction(ReactionDto reaction) {
		
		
		
		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		// Check if the user has the "ROLE_USER" role
		boolean hasUserRole = authorities.stream()
		        .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));	
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
	    // Log the username
	    System.out.println("Username: " + username); 
		// Call your account service to fetch the user by username
	    AppUser user = accountService.loadUserByUsername(username); 
		// Log or perform actions based on whether the user has the role
		if (hasUserRole) {
			
			if(reactionRepo.findById(reaction.getIdReaction()).get().getCustomerId()==user.getId()) {
				return serviceReaction.updateReaction(reaction);
			}else {
				return null;
			}	   
		} else {
			return serviceReaction.updateReaction(reaction);
		}
	}
	
	
	
	

}
