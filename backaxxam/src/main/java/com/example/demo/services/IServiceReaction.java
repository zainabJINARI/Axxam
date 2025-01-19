package com.example.demo.services;

import com.example.demo.dtos.PaginatedResponse;
import com.example.demo.dtos.ReactionDto;
import com.example.demo.exceptions.AnnouncementNotFoundException;

public interface IServiceReaction {
	public PaginatedResponse<ReactionDto> getAllReactionsByAnn(int page,int size,String idAnn);
	public PaginatedResponse<ReactionDto> getAllReactionsByUser(int page,int size,Long idUser);
	public PaginatedResponse<ReactionDto> getAllUnreadReactionsByHost(int page,int size,Long idHost);
	public Long createReaction(ReactionDto reaction) throws Exception;
	public void deleteReaction(Long reactionId);
	public ReactionDto updateReaction(ReactionDto reaction);
	boolean updateReactionStatus(Long idReaction, Long idHost) throws AnnouncementNotFoundException;

}
