package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.AnnouncementDto;
import com.example.demo.dtos.PaginatedResponse;
import com.example.demo.dtos.ReactionDto;
import com.example.demo.entities.Announcement;
import com.example.demo.entities.Reaction;
import com.example.demo.enums.NotificationStatus;
import com.example.demo.exceptions.AnnouncementNotFoundException;
import com.example.demo.mappers.ReactionMapper;
import com.example.demo.repositories.ReactionRepo;
import com.example.demo.security.repository.AppUserRepository;

import lombok.AllArgsConstructor;


@Service 
@AllArgsConstructor
public class ServiceReactionImpl implements IServiceReaction{
	
	private ReactionRepo reactionRepo;
	private ReactionMapper reactionMapper;
	@Autowired
	private IServiceAnnouncement announcementClient;
	
	
	@Autowired
	private AppUserRepository appUserRepository;
	
	
	@Override
	public boolean updateReactionStatus(Long idReaction,Long idHost) throws AnnouncementNotFoundException {
		Reaction reaction = reactionRepo.findById(idReaction).get();
		AnnouncementDto announcement =    announcementClient.getAnnouncementById(reaction.getAnnouncementId());
		if(announcement.getHost().getId()==idHost) {
			reaction.setStatus(NotificationStatus.READ);
			reactionRepo.save(reaction);
			return true;
		}else {
			return false;
			
		}
				
		
	}

	@Override
	public PaginatedResponse<ReactionDto> getAllReactionsByAnn(int page, int size, String idAnn) {
		// TODO Auto-generated method stub
//		check if this annoucnemeent exists 
		Page<Reaction> reactions = reactionRepo.findByAnnouncementId(idAnn,PageRequest.of(page,size));
		List<ReactionDto> reactionsDto =reactions.getContent().stream().map(r->{
			r.setCustomer(appUserRepository.findById(r.getCustomerId()).get());
			return reactionMapper.fromReaction(r);
		}).collect(Collectors.toList());
		
		
		return new PaginatedResponse<ReactionDto>(reactionsDto,reactions.getTotalElements());
	}

	@Override
	public PaginatedResponse<ReactionDto> getAllReactionsByUser(int page, int size, Long idUser) {
		// TODO Auto-generated method stub
		Page<Reaction> reactions = reactionRepo.findByCustomerId(idUser,PageRequest.of(page,size));
		List<ReactionDto> reactionsDto =reactions.getContent().stream().map(r->{
			r.setCustomer(appUserRepository.findById(r.getCustomerId()).get());
			return reactionMapper.fromReaction(r);
		}).collect(Collectors.toList());
		
		
		return new PaginatedResponse<ReactionDto>(reactionsDto,reactions.getTotalElements());
	}

	@Override
	public PaginatedResponse<ReactionDto> getAllUnreadReactionsByHost(int page, int size, Long idHost) {
	    try {
	        // Fetch announcements by hostId
	        List<AnnouncementDto> announcements = announcementClient.getAllAnnouncementsByHost(idHost, page, size).getItems();
	       
	     
	        // If no announcements are found, return an empty response
	        if (announcements.isEmpty()) {
	            return new PaginatedResponse<>(new ArrayList<>(), 0);
	        }


	        Set<Reaction> unreadReactionss = announcements.stream()
	        	    .flatMap(announcement -> {
	        	        Page<Reaction> reactionsPage = reactionRepo.findByStatus(
	        	            NotificationStatus.UNREAD,
	        	            PageRequest.of(0, Integer.MAX_VALUE) // Fetch all unread reactions for the announcement
	        	        );
	        	        return reactionsPage.getContent().stream();
	        	    })
	        	    .collect(Collectors.toSet()); // Use Set to remove duplicates
	        List<Reaction> unreadReactions = new ArrayList<>(unreadReactionss);



	        // Paginate the combined unread reactions
	        int start = Math.min(page * size, unreadReactions.size());
	        int end = Math.min(start + size, unreadReactions.size());
	        List<ReactionDto> paginatedReactions = unreadReactions.subList(start, end).stream()
	                .map(r->{
	                	ReactionDto rest = reactionMapper.fromReaction(r);
	                	
	                	rest.setCustomer(appUserRepository.findById(r.getCustomerId()).get());
	                	return rest;
	                	
	                }).collect(Collectors.toList());
	        
	        
	        

	        // Return a paginated response
	        return new PaginatedResponse<>(paginatedReactions, unreadReactions.size());

	    } catch (Exception e) {
	        // Log the error for debugging
	    	e.printStackTrace();

	        System.out.printf( e.getMessage());
	        return new PaginatedResponse<>(new ArrayList<>(), 0);
	    }
	}


	@Override
	public Long createReaction(ReactionDto reaction) throws Exception {
		// TODO Auto-generated method stub
//		check if customer exits
//		check if announceme,t exits
		Reaction reactionNew = reactionMapper.fromReactionDto(reaction);
		Reaction foundReaction = reactionRepo.findByAnnouncementId(reaction.getAnnouncementId())
			    .stream()
			    .filter(r -> r.getCustomerId().equals(reaction.getCustomerId()))
			    .findFirst().orElse(null);
		
		if(foundReaction!=null) {
			return (long) -1;
			
		}else {
			return reactionRepo.save(reactionNew).getIdReaction();
		}
		
//		Page<Reaction> reactions = reactionRepo.findByCustomerId(reaction.getCustomerId(),PageRequest.of(0,5));
//		System.out.println(reactions.getContent());
//		if(reactions.getContent().size()!=0) {
//			return (long) -1;
////			throw new Exception("Already reacted");
//			
//		}else {
//			return reactionRepo.save(reactionNew).getIdReaction();
//			
//		}
		
		
	}

	@Override
	public void deleteReaction(Long reactionId) {
		// TODO Auto-generated method stub
		
		reactionRepo.deleteById(reactionId);
		
	}

	@Override
	public ReactionDto updateReaction(ReactionDto reaction) {
		// TODO Auto-generated method stub
		Reaction reactionUpdate = reactionMapper.fromReactionDto(reaction);
		reactionUpdate.setIdReaction(reaction.getIdReaction());
		reactionUpdate.setCustomer(appUserRepository.findById(reactionUpdate.getCustomerId()).get());
		reactionRepo.save(reactionUpdate);
		return reactionMapper.fromReaction(reactionUpdate);
	}

}
