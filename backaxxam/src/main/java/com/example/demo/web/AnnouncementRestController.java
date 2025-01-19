package com.example.demo.web;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dtos.AnnouncementDto;
import com.example.demo.dtos.AnnouncementDtoCreate;
import com.example.demo.dtos.DetailedAnnouncementDto;
import com.example.demo.dtos.DetailedAnnouncementRating;
import com.example.demo.dtos.PaginatedResponse;
import com.example.demo.dtos.ServiceADto;
import com.example.demo.dtos.ServiceADtoCreate;
import com.example.demo.entities.Category;
import com.example.demo.entities.Reaction;
import com.example.demo.exceptions.AnnouncementNotFoundException;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ReactionRepo;
import com.example.demo.security.entities.AppUser;
import com.example.demo.security.repository.AppUserRepository;
import com.example.demo.security.services.AccountService;
import com.example.demo.services.IServiceAnnService;
import com.example.demo.services.IServiceAnnouncement;
import lombok.AllArgsConstructor;







@RestController
@AllArgsConstructor
@CrossOrigin

public class AnnouncementRestController {
	
	private IServiceAnnouncement serviceAnn;
	private IServiceAnnService servicesServAnn;
	private  AccountService accountService;
	private CategoryRepository categoryRepository;
	private ReactionRepo reactionRepo;
	
	private AppUserRepository appUserRepository;
	
	@GetMapping("/announcements/nbr")
	public int getNbrAnnouncements() {
		return serviceAnn.getNbrAnnouncements();
	}
	
	@GetMapping("/announcements/ordered-by-rating")
    public Page<DetailedAnnouncementRating> getAnnouncementsOrderedByRating(
    		   @RequestParam(name = "page", defaultValue = "0") int page,
   	        @RequestParam(name = "size", defaultValue = "5") int size) {

        return serviceAnn.getAnnouncementsOrderedByRating(page, size);
    }
	

	@GetMapping("/announcements/categories")
	public List<Category> getAllCategories(){
		return categoryRepository.findAll();
	}
	
	
	@GetMapping("/announcements/nbr/host/{id}")
	public int getNbrAnnouncements(@PathVariable Long id) {
		return serviceAnn.getNbrAnnouncementsByHostId(id);
	}
	
	
//	@GetMapping("/announcements")
//	public PaginatedResponse<AnnouncementDto> getAllAnnouncements(
//	        @RequestParam(name = "page", defaultValue = "0") int page,
//	        @RequestParam(name = "size", defaultValue = "5") int size,
//	        @RequestParam(name = "location", required = false) String location) {
//
//	    if (location != null && !location.isBlank()) {
//	        return serviceAnn.getFilteredAnnouncementsByLocation(page, size, location);
//	    }
//	    return serviceAnn.getAllAnnouncements(page, size);
//	}
	
	@GetMapping("/announcements")
	public PaginatedResponse<AnnouncementDto> getAllAnnouncements(
	        @RequestParam(name = "page", defaultValue = "0") int page,
	        @RequestParam(name = "size", defaultValue = "5") int size,
	        @RequestParam(name = "location", required = false) String location,
	        @RequestParam(name = "title", required = false) String title,
	        @RequestParam(name = "price", required = false) Double price,
	        @RequestParam(name = "categoryId", required = false) Long categoryId) {
		System.out.println(location);
		System.out.println(title);
		System.out.println(price);
		System.out.println(categoryId);
		

	    if (location != null && !location.isBlank()) {
	        return serviceAnn.getFilteredAnnouncementsByLocationAndMore(page, size, location, title, price, categoryId);
	    }
	    return serviceAnn.getAllAnnouncements(page, size);
	}

	
	
	
	

	
	
	@GetMapping("/announcements/{id}")
	public DetailedAnnouncementDto getAnnouncement(@PathVariable String  id) throws AnnouncementNotFoundException {
		List<Reaction> reactions = reactionRepo.findByAnnouncementId(id).stream().map(r ->{
			
			 AppUser user = appUserRepository.findById(r.getCustomerId()).get();
			 r.setCustomer(user);
			 return r;
			
			
		}).collect(Collectors.toList());
		
		
		AnnouncementDto announcementDto= serviceAnn.getAnnouncementById(id);
		
		return DetailedAnnouncementDto.builder()
				.id(announcementDto.getId())
				.title(announcementDto.getTitle())
				.description(announcementDto.getDescription())
				.address(announcementDto.getAddress())
				.priceForNight(announcementDto.getPriceForNight())
				.dateCreation(announcementDto.getDateCreation())
				.category(announcementDto.getCategory())
				.host(announcementDto.getHost())
				.services(announcementDto.getServices())
				.photos(announcementDto.getPhotos())
				.reactions(reactions)
				.build();
				
		
	}
	
//	add a path to get nbr of announceent for this host in total
	
	@PreAuthorize("hasAuthority('SCOPE_ROLE_HOST')")
	@GetMapping("/announcements/host")
	public  PaginatedResponse<AnnouncementDto> getAllAnnouncementProp(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size){
		
		 String username = SecurityContextHolder.getContext().getAuthentication().getName();

		    // Log the username
		    System.out.println("Username: " + username);

		   
			// Call your account service to fetch the user by username
		    AppUser user = accountService.loadUserByUsername(username); 
		
		return serviceAnn.getAllAnnouncementsByHost(user.getId(),page,size);
	}
	
	
	@PreAuthorize("hasAuthority('SCOPE_ROLE_HOST')")
	@PostMapping(value="/announcements",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Map<String, String> createAnnouncement( AnnouncementDtoCreate announcement) throws IOException {
		 String username = SecurityContextHolder.getContext().getAuthentication().getName();

		    // Log the username
		    System.out.println("Username: " + username);

		   
			// Call your account service to fetch the user by username
		    AppUser user = accountService.loadUserByUsername(username); 
		    announcement.setHostId(user.getId());
		
		announcement.getImages().forEach(image -> {
		        System.out.println("Received file: " + image.getOriginalFilename());
		        // Example: Save the image to disk or cloud storage
		    });
		
		
		return Map.of("id",serviceAnn.createAnnouncement(announcement));
	}
	
	
	@PreAuthorize("hasAuthority('SCOPE_ROLE_HOST')")
	@PostMapping(value="/announcements/services",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Long createServiceAnnouncement( ServiceADtoCreate service) throws IOException, AnnouncementNotFoundException {
		return servicesServAnn.createService(service);
	}
	
	
	@PreAuthorize("hasAnyAuthority('SCOPE_ROLE_HOST', 'SCOPE_ROLE_ADMIN')")
	@DeleteMapping("/announcements/{id}")
	public void deleteAnnouncement(@PathVariable String id) {
		
	   serviceAnn.deleteAnnouncement(id);
		
	}
	
	@PreAuthorize("hasAuthority('SCOPE_ROLE_HOST')")
	@DeleteMapping("/announcements/services/{id}")
	public void deleteAnnouncementService(@PathVariable Long id) throws Exception {
		
		servicesServAnn.deleteService(id);
			
		}
	
	
	
	
	@PreAuthorize("hasAuthority('SCOPE_ROLE_HOST')")
	@PutMapping(value="/announcements",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public AnnouncementDto updateAnnouncement( AnnouncementDtoCreate announcement) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

	    // Log the username
	    System.out.println("Username: " + username);

	   
		// Call your account service to fetch the user by username
	    AppUser user = accountService.loadUserByUsername(username); 
	    announcement.setHostId(user.getId());
		return serviceAnn.updateAnnouncement(announcement);
	}
	
	
	@PreAuthorize("hasAuthority('SCOPE_ROLE_HOST')")
	@PutMapping(value="/announcements/services",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ServiceADto updateServiceAnnouncement(ServiceADtoCreate service) throws IOException {
		return servicesServAnn.updateService(service);
	}
	

}
