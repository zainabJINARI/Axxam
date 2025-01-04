package com.example.demo.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.example.demo.dto.AnnouncementDto;
import com.example.demo.dto.AnnouncementDtoCreate;
import com.example.demo.dto.PaginatedResponse;
import com.example.demo.entities.Announcement;
import com.example.demo.exceptions.AnnouncementNotFoundException;

public interface IServiceAnnouncement {
	public String createAnnouncement(AnnouncementDtoCreate announcement) throws IOException;
	public AnnouncementDto updateAnnouncement( AnnouncementDtoCreate announcement);
	public void deleteAnnouncement(String id);
	public AnnouncementDto getAnnouncementById(String id) throws AnnouncementNotFoundException;
	public PaginatedResponse<AnnouncementDto> getAllAnnouncements(int page, int size);
	public List<AnnouncementDto> getAllAnnouncementsByHost(Long id,int page, int size);
	public List<Announcement> filterAnnouncements();
	public int getNbrAnnouncements();
	public int getNbrAnnouncementsByHostId(Long id);
	public PaginatedResponse<AnnouncementDto> getFilteredAnnouncementsByLocation(int page, int size, String address); 
	
	
	

}
