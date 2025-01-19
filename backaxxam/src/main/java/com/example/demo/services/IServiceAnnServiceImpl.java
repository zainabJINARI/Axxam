package com.example.demo.services;


import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dtos.ServiceADto;
import com.example.demo.dtos.ServiceADtoCreate;
import com.example.demo.entities.Announcement;
import com.example.demo.entities.ServiceA;
import com.example.demo.exceptions.AnnouncementNotFoundException;
import com.example.demo.mappers.ServiceaMapper;
import com.example.demo.repositories.AnnouncementRepository;
import com.example.demo.repositories.ServiceaRepository;




@Service
public class IServiceAnnServiceImpl implements IServiceAnnService {
	
//	@Autowired
//	private AnnouncementRepository announcementRepo ;
	
	@Autowired
	private ServiceaRepository serviceaRepo ;
	
	@Autowired
	private AnnouncementRepository annRepo;
	
	@Autowired
	private  ServiceaMapper mapperService ;
	
	

	@Override
	public List<ServiceADto> getAllServicesByAnnId(String  id)  {
//		  check if the  announcement exist first	
			return serviceaRepo.findByAnnouncementId(id).stream().map(ser->{
				return mapperService.fromServiceA(ser);
				
			}).collect(Collectors.toList());
	}
	@Override
	public Long createService(ServiceADtoCreate s) throws IOException, AnnouncementNotFoundException {
		// TODO Auto-generated method stub
		
		Path path = Paths.get(System.getProperty("user.home"),"announcement-app-files","services-images");
		if(!Files.exists(path)) {
			Files.createDirectories(path);
		}
		String imageId = UUID.randomUUID().toString();
		Path imagePath = Paths.get(System.getProperty("user.home"),"announcement-app-files","services-images",imageId+".png");
		Files.copy(s.getImage().getInputStream(),imagePath);
		
		Announcement ann= annRepo.findById(s.getAnnouncementId()).get();
		if(ann==null) {
			throw new AnnouncementNotFoundException("The announcement is not found");
		}
		
		ServiceA serv = ServiceA.builder()
				.title(s.getTitle())
				.description(s.getDescription())
				.photo(imagePath.toUri().toString())
				.announcementId(s.getAnnouncementId())
				.build();
		
		
		return serviceaRepo.save(serv).getId();
	}
	@Override
	public ServiceADto updateService(ServiceADtoCreate s) throws IOException {
		// TODO Auto-generated method stub
		ServiceA existingServicea = serviceaRepo.findById(s.getId()).get();
		if(existingServicea!=null) {
			existingServicea.setTitle(s.getTitle());
			existingServicea.setDescription(s.getDescription());
			existingServicea.setAnnouncementId(s.getAnnouncementId());
			if(s.getImage()!=null) {
				try {
					Files.deleteIfExists(Paths.get(URI.create(existingServicea.getPhoto())));
				} catch (IOException e) {
					System.err.println("Failed to delete old image of service at path: " + existingServicea.getPhoto());
					e.printStackTrace();
				}
				Path path = Paths.get(System.getProperty("user.home"),"announcement-app-files","services-images");
				String imageId = UUID.randomUUID().toString();
				Path imagePath = Paths.get(System.getProperty("user.home"),"announcement-app-files","services-images",imageId+".png");
				Files.copy(s.getImage().getInputStream(),imagePath);
				existingServicea.setPhoto(imagePath.toUri().toString());
				
			}
			
			serviceaRepo.save(existingServicea);
			
		}else {
			new IllegalArgumentException("Service not found for ID: " + s.getId());
		}
		return mapperService.fromServiceA(existingServicea);
	}
	
	// Helper method to encode image to Base64
	private String encodeImageToBase64(Path imagePath) {
	    try {
	        byte[] imageBytes = Files.readAllBytes(imagePath);
	        return Base64.getEncoder().encodeToString(imageBytes);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
//	@Override
//	public List<Map<Long, String>> getServicesImgsByAnnouncement(String id) {
//		// TODO Auto-generated method stub
//		
//		List<ServiceA> services = getAllServicesByAnnId(id);
//		List<Map<Long, String>> list = services.stream().map(s -> {
//		    try {
//		        // Sanitize the photo path
//		        String sanitizedPhoto = s.getPhoto().replace("file:///", "").replace("file:/", "");
//
//		        // Construct the path
//		        Path path;
//		        if (Paths.get(sanitizedPhoto).isAbsolute()) {
//		            path = Paths.get(sanitizedPhoto);
//		        } else {
//		            path = Paths.get(System.getProperty("user.home"), "announcement-app-files", "services-images", sanitizedPhoto);
//		        }
//
//		        // Convert to Base64
//		        String base64Image = encodeImageToBase64(path);
//
//		        // Create a map and add serviceId and imageBase64
//		        Map<Long, String> serviceMap = new HashMap<>();
//		        serviceMap.put(s.getId(), base64Image);
//
//		        return serviceMap;
//
//		    } catch (Exception e) {
//		        e.printStackTrace(); // Log the error
//		        return Map.of(s.getId(), "Error encoding image"); // Graceful fallback
//		    }
//		}).collect(Collectors.toList());
//
//
//
//		return list ;
//		
//		
//		
//	}
//	
	@Override
	public void deleteService(Long id) throws Exception {
		// TODO Auto-generated method stub
		
		ServiceA s = serviceaRepo.findById(id).get();
		if(s==null) {
			throw new Exception("Not found");
		}
		
		
		try {
			if (s.getPhoto().startsWith("file:///")) {
				s.setPhoto(s.getPhoto().substring(8)) ; // Remove "file:///"
			}
			Path path = Paths.get(s.getPhoto());
			Files.deleteIfExists(path); // Delete the file if it exists
		} catch (IOException e) {
			System.err.println("Failed to delete image at path: " + s.getPhoto());
			e.printStackTrace();
		}
		serviceaRepo.deleteById(s.getId());
		
	}


}
