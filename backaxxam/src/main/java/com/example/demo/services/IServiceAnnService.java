package com.example.demo.services;

import java.io.IOException;
import java.util.List;
import com.example.demo.dtos.ServiceADto;
import com.example.demo.dtos.ServiceADtoCreate;
import com.example.demo.exceptions.AnnouncementNotFoundException;

public interface IServiceAnnService {
	
	public List<ServiceADto> getAllServicesByAnnId(String id);
	public Long createService(ServiceADtoCreate s) throws IOException, AnnouncementNotFoundException;
	public ServiceADto updateService(ServiceADtoCreate s) throws IOException;
//	public List<Map<Long, String>> getServicesImgsByAnnouncement(String id);
	
	public void deleteService(Long id) throws Exception;

}
