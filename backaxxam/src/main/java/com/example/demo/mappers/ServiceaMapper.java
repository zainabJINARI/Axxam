package com.example.demo.mappers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import org.springframework.stereotype.Service;
import com.example.demo.dtos.ServiceADto;
import com.example.demo.entities.ServiceA;




@Service
public class ServiceaMapper {
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
	public ServiceADto fromServiceA(ServiceA s) {
		
		
		String sanitizedPhoto = s.getPhoto().replace("file:///", "").replace("file:/", "");

        // Construct the path
        Path path;
        if (Paths.get(sanitizedPhoto).isAbsolute()) {
            path = Paths.get(sanitizedPhoto);
        } else {
            path = Paths.get(System.getProperty("user.home"), "announcement-app-files", "services-images", sanitizedPhoto);
        }

        // Convert to Base64
        String base64Image = encodeImageToBase64(path);
		
		return  ServiceADto.builder()
				.id(s.getId())
				.title(s.getTitle())
				.description(s.getDescription())
				.announcementId(s.getAnnouncementId())
				.image(base64Image)
				.build();
	}

}
