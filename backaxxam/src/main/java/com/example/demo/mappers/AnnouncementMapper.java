package com.example.demo.mappers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.example.demo.dtos.AnnouncementDto;
import com.example.demo.dtos.ServiceADto;
import com.example.demo.entities.Announcement;
import com.example.demo.models.HostModel;

@Service
public class AnnouncementMapper {
	
	
	@Autowired
	private ServiceaMapper serviceAMapper;
	
//	helper
	private  String encodeImageToBase64(Path imagePath) {
        try {
            byte[] imageBytes = Files.readAllBytes(imagePath);
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // In case of error, return null or handle it as per your requirement
        }
    }
	
	
	

	public AnnouncementDto fromAnnouncement(Announcement a) {
		List<ServiceADto> services = a.getServices()!=null ? a.getServices().stream().map(s->{
			return serviceAMapper.fromServiceA(s);
		}).collect(Collectors.toList()) : new ArrayList<ServiceADto>();
		
		
		List<String> images = a.getPhotos().stream().map(i->{
			String sanitizedPhotoPath = i.replace("file:///", "").replace("file:/", "");

            // Construct the full path
            Path path;
            if (Paths.get(sanitizedPhotoPath).isAbsolute()) {
                path = Paths.get(sanitizedPhotoPath);
            } else {
                path = Paths.get(System.getProperty("user.home"), "announcement-app-files", "generalimages", sanitizedPhotoPath);
            }

            // Convert to Base64
            return encodeImageToBase64(path);
			
		}).collect(Collectors.toList());
		
		AnnouncementDto an = AnnouncementDto.builder()
				.id(a.getId())
				.title(a.getTitle())
				.description(a.getDescription())
				.address(a.getAddress())
				.priceForNight(a.getPriceForNight())
				.dateCreation(a.getDateCreation())
				.category(a.getCategory())
				.host(a.getHost())
				.services(services)	
				.photos(images)
				.build();
		return an;
		
	}

}
