package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.PaginatedResponse;
import com.example.demo.dtos.ReactionDto;

public interface IServiceReaction {
	public PaginatedResponse<ReactionDto> getAllReactionsByAnn(int page,int size,String idAnn);
	public PaginatedResponse<ReactionDto> getAllReactionsByUser(int page,int size,Long idUser);
	public PaginatedResponse<ReactionDto> getAllUnreadReactionsByHost(int page,int size,Long idHost);
	public Long createReaction(ReactionDto reaction);
	public void deleteReaction(Long reactionId);
	public ReactionDto updateReaction(ReactionDto reaction);

}
