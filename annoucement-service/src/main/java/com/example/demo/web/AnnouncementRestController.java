package com.example.demo.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AnnouncementDto;
import com.example.demo.dto.AnnouncementDtoCreate;
import com.example.demo.dto.PaginatedResponse;
import com.example.demo.dto.ServiceADto;
import com.example.demo.dto.ServiceADtoCreate;
import com.example.demo.exceptions.AnnouncementNotFoundException;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.services.IServiceAnnService;
import com.example.demo.services.IServiceAnnServiceImpl;
import com.example.demo.services.IServiceAnnouncement;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AnnouncementRestController {
	
	private IServiceAnnouncement serviceAnn;
	
	private IServiceAnnService servicesServAnn;
	
	@GetMapping("/announcements/nbr")
	public int getNbrAnnouncements() {
		return serviceAnn.getNbrAnnouncements();
	}
	
	@GetMapping("/announcements/nbr/host/{id}")
	public int getNbrAnnouncements(@PathVariable Long id) {
		return serviceAnn.getNbrAnnouncementsByHostId(id);
	}
	
	
	@GetMapping("/announcements")
	public PaginatedResponse<AnnouncementDto> getAllAnnouncements(
	        @RequestParam(name = "page", defaultValue = "0") int page,
	        @RequestParam(name = "size", defaultValue = "5") int size,
	        @RequestParam(name = "location", required = false) String location) {

	    if (location != null && !location.isBlank()) {
	        return serviceAnn.getFilteredAnnouncementsByLocation(page, size, location);
	    }
	    return serviceAnn.getAllAnnouncements(page, size);
	}
	
	
	
	

	
	
	@GetMapping("/announcements/{id}")
	public AnnouncementDto getAnnouncement(@PathVariable String  id) throws AnnouncementNotFoundException {
		return serviceAnn.getAnnouncementById(id);
		
	}
	
//	add a path to get nbr of announceent for this host in total
	
	@GetMapping("/announcements/host/{id}")
	public List<AnnouncementDto> getAllAnnouncementProp(@PathVariable Long  id, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size){
		return serviceAnn.getAllAnnouncementsByHost(id,page,size);
	}
	
	@PostMapping(value="/announcements",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String createAnnouncement( AnnouncementDtoCreate announcement) throws IOException {
		
		announcement.getImages().forEach(image -> {
		        System.out.println("Received file: " + image.getOriginalFilename());
		        // Example: Save the image to disk or cloud storage
		    });
		
		return serviceAnn.createAnnouncement(announcement);
	}
	
	
	@PostMapping(value="/announcements/services",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Long createServiceAnnouncement( ServiceADtoCreate service) throws IOException, AnnouncementNotFoundException {
		return servicesServAnn.createService(service);
	}
	
	
	@DeleteMapping("/announcements/{id}")
	public void deleteAnnouncement(@PathVariable String id) {
		
	   serviceAnn.deleteAnnouncement(id);
		
	}
	
	
	@PutMapping(value="/announcements",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public AnnouncementDto updateAnnouncement( AnnouncementDtoCreate announcement) {
		return serviceAnn.updateAnnouncement(announcement);
	}
	
	@PutMapping(value="/announcements/services",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ServiceADto updateServiceAnnouncement(ServiceADtoCreate service) throws IOException {
		
		return servicesServAnn.updateService(service);
	}
	
	
	

}
