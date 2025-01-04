package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.client.AnnouncementRestClient;
import com.example.demo.dtos.PaginatedResponse;
import com.example.demo.dtos.ReactionDto;
import com.example.demo.entities.Reaction;
import com.example.demo.enums.NotificationStatus;
import com.example.demo.mappers.ReactionMapper;
import com.example.demo.models.AnnouncementModel;
import com.example.demo.repositories.ReactionRepo;

import lombok.AllArgsConstructor;


@Service 
@AllArgsConstructor
public class ServiceReactionImpl implements IServiceReaction{
	
	private ReactionRepo reactionRepo;
	private ReactionMapper reactionMapper;
	private AnnouncementRestClient announcementClient;

	@Override
	public PaginatedResponse<ReactionDto> getAllReactionsByAnn(int page, int size, String idAnn) {
		// TODO Auto-generated method stub
//		check if this annoucnemeent exists 
		Page<Reaction> reactions = reactionRepo.findByAnnouncementId(idAnn,PageRequest.of(page,size));
		List<ReactionDto> reactionsDto =reactions.getContent().stream().map(r->{
			return reactionMapper.fromReaction(r);
		}).collect(Collectors.toList());
		
		
		return new PaginatedResponse<ReactionDto>(reactionsDto,reactions.getTotalElements());
	}

	@Override
	public PaginatedResponse<ReactionDto> getAllReactionsByUser(int page, int size, Long idUser) {
		// TODO Auto-generated method stub
		Page<Reaction> reactions = reactionRepo.findByCustomerId(idUser,PageRequest.of(page,size));
		List<ReactionDto> reactionsDto =reactions.getContent().stream().map(r->{
			return reactionMapper.fromReaction(r);
		}).collect(Collectors.toList());
		
		
		return new PaginatedResponse<ReactionDto>(reactionsDto,reactions.getTotalElements());
	}

	@Override
	public PaginatedResponse<ReactionDto> getAllUnreadReactionsByHost(int page, int size, Long idHost) {
	    try {
	        // Fetch announcements by hostId
	        List<AnnouncementModel> announcements = announcementClient.getAllAnnouncementProp(idHost);

	        // If no announcements are found, return an empty response
	        if (announcements.isEmpty()) {
	            return new PaginatedResponse<>(new ArrayList<>(), 0);
	        }

	        // Gather all unread reactions for all announcements
	        List<Reaction> unreadReactions = announcements.stream()
	                .flatMap(announcement -> {
	                    // Fetch unread reactions for each announcement
	                    Page<Reaction> reactionsPage = reactionRepo.findByStatus(
	                            NotificationStatus.UNREAD,
	                            PageRequest.of(0, Integer.MAX_VALUE) // Fetch all unread reactions for the announcement
	                    );
	                    return reactionsPage.getContent().stream(); // Flatten to a single stream of reactions
	                })
	                .collect(Collectors.toList());

	        // Paginate the combined unread reactions
	        int start = Math.min(page * size, unreadReactions.size());
	        int end = Math.min(start + size, unreadReactions.size());
	        List<ReactionDto> paginatedReactions = unreadReactions.subList(start, end).stream()
	                .map(reactionMapper::fromReaction)
	                .collect(Collectors.toList());

	        // Return a paginated response
	        return new PaginatedResponse<>(paginatedReactions, unreadReactions.size());

	    } catch (Exception e) {
	        // Log the error for debugging
	        System.out.printf("Error fetching unread reactions for hostId: " + idHost, e);
	        return new PaginatedResponse<>(new ArrayList<>(), 0);
	    }
	}


	@Override
	public Long createReaction(ReactionDto reaction) {
		// TODO Auto-generated method stub
//		check if customer exits
//		check if announceme,t exits
		Reaction reactionNew = reactionMapper.fromReactionDto(reaction);
		
		return reactionRepo.save(reactionNew).getIdReaction();
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
		
		return reactionMapper.fromReaction(reactionRepo.save(reactionUpdate));
	}

}
