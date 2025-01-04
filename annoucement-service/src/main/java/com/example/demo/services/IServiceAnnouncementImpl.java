package com.example.demo.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AnnouncementDto;
import com.example.demo.dto.AnnouncementDtoCreate;
import com.example.demo.dto.PaginatedResponse;
import com.example.demo.entities.Announcement;
import com.example.demo.entities.Category;
import com.example.demo.entities.ServiceA;
import com.example.demo.exceptions.AnnouncementNotFoundException;
import com.example.demo.mappers.AnnouncementMapper;
import com.example.demo.repositories.AnnouncementRepository;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ServiceaRepository;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class IServiceAnnouncementImpl implements IServiceAnnouncement {

	@Autowired
	private AnnouncementRepository annRepo;

	@Autowired
	private ServiceaRepository serviceRepo;

	@Autowired
	private AnnouncementMapper mapperAnn;
	@Autowired
	private IServiceAnnService serviceAnnS;

	@Autowired
	private CategoryRepository catRep;
	
	
	@Override
	public PaginatedResponse<AnnouncementDto> getFilteredAnnouncementsByLocation(int page, int size, String location) {
	    Page<Announcement> announcements = annRepo.findByAddressContainingIgnoreCase(location, PageRequest.of(page, size));
	    List<Announcement> announcementsList =announcements.getContent().stream().map(a -> {

//			get services assoacited to announcement
			a.setServices(new ArrayList<>(serviceRepo.findByAnnouncementId(a.getId())));

			return a;
		}).collect(Collectors.toList());
	
	    List<AnnouncementDto> list= announcements.getContent().stream().map(a->{
	    	return mapperAnn.fromAnnouncement(a);
	    	
	    }).collect(Collectors.toList());
	    
	   return new PaginatedResponse<AnnouncementDto>(list, announcements.getTotalElements());
	}



	@Override
	public String createAnnouncement(AnnouncementDtoCreate announcement) throws IOException {
		
		
		
//		genrate random id
		String id = UUID.randomUUID().toString();
//		store images in file system
		Path path = Paths.get(System.getProperty("user.home"), "announcement-app-files", "generalimages");
		if (!Files.exists(path)) {
			Files.createDirectories(path);
		}

		List<String> imagePaths = new ArrayList<String>();

		announcement.getImages().forEach(img -> {
			String imageId = UUID.randomUUID().toString();
			Path imagePath = Paths.get(System.getProperty("user.home"), "announcement-app-files", "generalimages",
					imageId + ".png");
			try {
				Files.copy(img.getInputStream(), imagePath);
				imagePaths.add(imagePath.toUri().toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		System.out.println(announcement);
//		Category c = catRep.findById(announcement.getCategoryId()).get();
		Category c = catRep.findById(announcement.getCategoryId()).orElseThrow(
				() -> new IllegalArgumentException("Category not found for ID: " + announcement.getCategoryId()));
		Announcement an = Announcement.builder().id(id).title(announcement.getTitle())
				.description(announcement.getDescription()).address(announcement.getAddress())
				.priceForNight(announcement.getPriceForNight()).dateCreation(new Date()).category(c).photos(imagePaths)
				.hostId(announcement.getHostId())
				.build();

		annRepo.save(an);
		return id;

	}

	@Override
	public AnnouncementDto updateAnnouncement(AnnouncementDtoCreate announcementDto) {

		Announcement existingAnnouncement = annRepo.findById(announcementDto.getId()).orElseThrow(
				() -> new IllegalArgumentException("Announcement not found for ID: " + announcementDto.getId()));

		// Update general information
		existingAnnouncement.setTitle(announcementDto.getTitle());
		existingAnnouncement.setDescription(announcementDto.getDescription());
		existingAnnouncement.setAddress(announcementDto.getAddress());
		existingAnnouncement.setPriceForNight(announcementDto.getPriceForNight());

		// Handle category update if necessary
		if (announcementDto.getCategoryId() != null
				&& !existingAnnouncement.getCategory().getIdC().equals(announcementDto.getCategoryId())) {
			Category newCategory = catRep.findById(announcementDto.getCategoryId())
					.orElseThrow(() -> new IllegalArgumentException(
							"Category not found for ID: " + announcementDto.getCategoryId()));
			existingAnnouncement.setCategory(newCategory);
		}

		// Update photos: Delete old photos and save new ones
		if (announcementDto.getImages() != null) {
			// Delete old photos from the file system
			if (existingAnnouncement.getPhotos() != null) {
				existingAnnouncement.getPhotos().forEach(photoPath -> {
					try {
						Files.deleteIfExists(Paths.get(URI.create(photoPath)));
					} catch (IOException e) {
						System.err.println("Failed to delete old image at path: " + photoPath);
						e.printStackTrace();
					}
				});
			}

			// Save new photos
			List<String> newPhotoPaths = new ArrayList<>();
			Path imageDirectory = Paths.get(System.getProperty("user.home"), "announcement-app-files", "generalimages");
			if (!Files.exists(imageDirectory)) {
				try {
					Files.createDirectories(imageDirectory);
				} catch (IOException e) {
					throw new RuntimeException("Failed to create image directory", e);
				}
			}

			announcementDto.getImages().forEach(img -> {
				String newImageId = UUID.randomUUID().toString();
				Path imagePath = Paths.get(imageDirectory.toString(), newImageId + ".png");
				try {
					Files.copy(img.getInputStream(), imagePath);
					newPhotoPaths.add(imagePath.toUri().toString());
				} catch (IOException e) {
					System.err.println("Failed to save new image: " + newImageId);
					e.printStackTrace();
				}
			});

			// Update the photos in the announcement
			existingAnnouncement.setPhotos(newPhotoPaths);
		}

		// Save the updated announcement
		annRepo.save(existingAnnouncement);

		System.out.println("Announcement with ID " + existingAnnouncement.getId() + " has been updated.");
		existingAnnouncement.setServices(serviceRepo.findByAnnouncementId(existingAnnouncement.getId()));
		
		return mapperAnn.fromAnnouncement(existingAnnouncement);
	}

	@Override
	public AnnouncementDto getAnnouncementById(String id) throws AnnouncementNotFoundException {
		Announcement announcement = annRepo.findById(id).get();
		if (announcement != null) {
			List<ServiceA> list = serviceRepo.findByAnnouncementId(id);
			announcement.setServices(list);

//			
			AnnouncementDto foundAnn = mapperAnn.fromAnnouncement(announcement);
			return foundAnn;

		} else {
			throw new AnnouncementNotFoundException("The announcement is not found");
		}

	}

	@Override
	public void deleteAnnouncement(String id) {
		Announcement announcement = annRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Announcement not found for ID: " + id));
		List<ServiceA> list = serviceRepo.findByAnnouncementId(id);
		announcement.setServices(list);

		// Delete associated images from the file system
		if (announcement.getPhotos() != null) {
			announcement.getPhotos().forEach(photoPath -> {
				try {
					Path path = Paths.get(photoPath);
					Files.deleteIfExists(path); // Delete the file if it exists
				} catch (IOException e) {
					System.err.println("Failed to delete image at path: " + photoPath);
					e.printStackTrace();
				}
			});
		}

		announcement.getServices().forEach(s -> {
			try {
				Path path = Paths.get(s.getPhoto());
				Files.deleteIfExists(path); // Delete the file if it exists
			} catch (IOException e) {
				System.err.println("Failed to delete image at path: " + s.getPhoto());
				e.printStackTrace();
			}
			serviceRepo.deleteById(s.getId());

		});

		// Delete the announcement from the database
		annRepo.deleteById(id);

		System.out.println("Announcement with ID " + id + " and associated images have been deleted.");

	}

	@Override
	public PaginatedResponse<AnnouncementDto> getAllAnnouncements(int page,int size) {
		Page<Announcement> announcement = annRepo.findAll(PageRequest.of(page, size));
		
//		get announcement data from db
		List<Announcement> announcements =announcement.getContent().stream().map(a -> {

//			get services assoacited to announcement
			a.setServices(new ArrayList<>(serviceRepo.findByAnnouncementId(a.getId())));

			return a;
		}).collect(Collectors.toList());

//		return formated announcement (DTO) version

		 List<AnnouncementDto> list =  announcements.stream().map(a -> {
			return mapperAnn.fromAnnouncement(a);
		}).collect(Collectors.toList());
		 
		 return new  PaginatedResponse<AnnouncementDto>(list, announcement.getTotalElements());
	}

	@Override
	public List<AnnouncementDto> getAllAnnouncementsByHost(Long id,int page,int size) {
		// TODO Auto-generated method stub
		Page<Announcement> announcement =annRepo.findByHostId(id,PageRequest.of(page, size));
		List<Announcement> announcements =announcement.getContent().stream().map(a -> {

//			get services assoacited to announcement
			a.setServices(new ArrayList<>(serviceRepo.findByAnnouncementId(a.getId())));

			return a;
		}).collect(Collectors.toList());

//		return formated announcement (DTO) version

		return announcements.stream().map(a -> {
			return mapperAnn.fromAnnouncement(a);
		}).collect(Collectors.toList());

	}

	@Override
	public List<Announcement> filterAnnouncements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNbrAnnouncements() {
		
		return annRepo.findAll().size();
	}
	
	@Override
	public int getNbrAnnouncementsByHostId(Long id) {
		return annRepo.findByHostId(id).size();
		
	}
	


//	@Override
//	public List<List<String>> getAllImgAnnouncements(int page, int size) {
//		// TODO Auto-generated method stub
//		Page<Announcement> announcementt = annRepo.findAll(PageRequest.of(page, size));
//		List<List<String>> imagesPaths = announcementt.getContent().stream()
//	            .map(announcement -> {
//	                // For each announcement, map each photo path (String) to its Base64 representation
//	                return announcement.getPhotos().stream()
//	                    .map(photoPath -> {
//	                        // Convert the image file path (stored as String) to Base64
//	                    	String sanitizedPhotoPath = photoPath.replace("file:///", "").replace("file:/", "");
//
//	                        // Construct the full path
//	                        Path path;
//	                        if (Paths.get(sanitizedPhotoPath).isAbsolute()) {
//	                            path = Paths.get(sanitizedPhotoPath);
//	                        } else {
//	                            path = Paths.get(System.getProperty("user.home"), "announcement-app-files", "generalimages", sanitizedPhotoPath);
//	                        }
//
//	                        // Convert to Base64
//	                        return encodeImageToBase64(path);
//	                    })
//	                    .collect(Collectors.toList());
//	            })
//	            .collect(Collectors.toList());
//		
//		
//		return imagesPaths;
//	}

	
	
	
	

}
