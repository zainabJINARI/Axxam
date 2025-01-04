package com.example.demo.web;

import java.sql.Date;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.PaginatedResponse;
import com.example.demo.dtos.ReactionDto;
import com.example.demo.enums.NotificationStatus;
import com.example.demo.models.AnnouncementModel;
import com.example.demo.models.CustomerModel;
import com.example.demo.services.IServiceReaction;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class ReactionController {
	
	private IServiceReaction serviceReaction;
	
	
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
	
	@GetMapping("/reactions/host/{hostId}")
	public PaginatedResponse<ReactionDto> getAllUnreadReactionsByHost(
			 @RequestParam(name = "page", defaultValue = "0") int page,
		        @RequestParam(name = "size", defaultValue = "5") int size,
		        @PathVariable Long hostId
			){
		return serviceReaction.getAllUnreadReactionsByHost(page,size,hostId);
	}
	
	
	@PostMapping("/reactions")
	public Long createReaction(ReactionDto reaction) {
		
		return serviceReaction.createReaction(reaction);
		
	}
	
	@DeleteMapping("/reactions/{id}")
	public void deleteReaction(@PathVariable Long id) {
		 serviceReaction.deleteReaction(id);
	}
	
	@PutMapping("/reactions")
	public ReactionDto updateReaction(ReactionDto reaction) {
		return serviceReaction.updateReaction(reaction);
	}
	
	
	
	

}
