package com.example.demo.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.example.demo.dto.ServiceADto;
import com.example.demo.dto.ServiceADtoCreate;
import com.example.demo.entities.ServiceA;
import com.example.demo.exceptions.AnnouncementNotFoundException;

public interface IServiceAnnService {
	
	public List<ServiceADto> getAllServicesByAnnId(String id);
	public Long createService(ServiceADtoCreate s) throws IOException, AnnouncementNotFoundException;
	public ServiceADto updateService(ServiceADtoCreate s) throws IOException;
//	public List<Map<Long, String>> getServicesImgsByAnnouncement(String id);

}
