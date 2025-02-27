package com.example.demo.mappers;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.ReactionDto;
import com.example.demo.entities.Reaction;

@Service
public class ReactionMapper {
	
	public ReactionDto fromReaction(Reaction reaction) {
		
		return  ReactionDto.builder()
				.idReaction(reaction.getIdReaction())
				.ratingValue(reaction.getRatingValue())
				.comment(reaction.getComment())
				.postedDate(reaction.getPostedDate())
				.customerId(reaction.getCustomerId())
				.announcementId(reaction.getAnnouncementId())
				.status(reaction.getStatus())
				.customer(reaction.getCustomer())
				.build();
		
	}
	
	public Reaction fromReactionDto(ReactionDto reaction) {
		return Reaction.builder()
			
				.ratingValue(reaction.getRatingValue())
				.comment(reaction.getComment())
				.postedDate(reaction.getPostedDate())
				.customerId(reaction.getCustomerId())
				.announcementId(reaction.getAnnouncementId())
				.status(reaction.getStatus())
				.build();
	}

}
