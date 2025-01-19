package com.example.demo.services;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.dtos.AnnouncementDto;
import com.example.demo.dtos.AnnouncementDtoCreate;
import com.example.demo.dtos.DetailedAnnouncementRating;
import com.example.demo.dtos.PaginatedResponse;
import com.example.demo.entities.Announcement;
import com.example.demo.exceptions.AnnouncementNotFoundException;





public interface IServiceAnnouncement {
	public String createAnnouncement(AnnouncementDtoCreate announcement) throws IOException;
	public AnnouncementDto updateAnnouncement( AnnouncementDtoCreate announcement);
	public void deleteAnnouncement(String id);
	public AnnouncementDto getAnnouncementById(String id) throws AnnouncementNotFoundException;
	public PaginatedResponse<AnnouncementDto> getAllAnnouncements(int page, int size);
	public PaginatedResponse<AnnouncementDto> getAllAnnouncementsByHost(Long id,int page, int size);
	public List<Announcement> filterAnnouncements();
	public int getNbrAnnouncements();
	public int getNbrAnnouncementsByHostId(Long id);
	public PaginatedResponse<AnnouncementDto> getFilteredAnnouncementsByLocation(int page, int size, String address);
	public Page<DetailedAnnouncementRating> getAnnouncementsOrderedByRating(int page, int size);
	PaginatedResponse<AnnouncementDto> getFilteredAnnouncementsByLocationAndMore(int page, int size, String location,
			String title, Double price, Long categoryId); 

	
	
	

}
