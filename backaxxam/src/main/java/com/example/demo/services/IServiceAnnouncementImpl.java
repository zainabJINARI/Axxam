package com.example.demo.services;
import java.util.List;
import java.util.Objects;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.example.demo.dtos.AnnouncementDto;
import com.example.demo.dtos.AnnouncementDtoCreate;
import com.example.demo.dtos.AnnouncementsIdByAvg;
import com.example.demo.dtos.DetailedAnnouncementRating;
import com.example.demo.dtos.PaginatedResponse;
import com.example.demo.entities.Announcement;
import com.example.demo.entities.Category;
import com.example.demo.entities.Reaction;
import com.example.demo.entities.ServiceA;
import com.example.demo.exceptions.AnnouncementNotFoundException;
import com.example.demo.mappers.AnnouncementMapper;
import com.example.demo.repositories.AnnouncementRepository;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ReactionRepo;
import com.example.demo.repositories.ServiceaRepository;
import com.example.demo.security.repository.AppUserRepository;
import com.example.demo.security.services.AccountService;

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
	
	@Autowired
	private AppUserRepository appUserRepository;
	
	 @Autowired
	    private ReactionRepo reactionRepository;
	 
	
	 public String fromImgPathToBase64(String i) {
			String sanitizedPhotoPath = i.replace("file:///", "").replace("file:/", "");

	        // Construct the full path
	        Path path;
	        if (Paths.get(sanitizedPhotoPath).isAbsolute()) {
	            path = Paths.get(sanitizedPhotoPath);
	        } else {
	            path = Paths.get(System.getProperty("user.home"), "announcement-app-files", "generalimages", sanitizedPhotoPath);
	        }
	        try {
	            byte[] imageBytes = Files.readAllBytes(path);
	            return Base64.getEncoder().encodeToString(imageBytes);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null; // In case of error, return null or handle it as per your requirement
	        }
			
		}
	 	 
	
	 
	 @Override
	 public Page<DetailedAnnouncementRating> getAnnouncementsOrderedByRating(int page, int size) {
	        // Fetch paginated reactions grouped by announcementId
	        Page<Object[]> reactions = reactionRepository.findAnnouncementIdsOrderedByAvgRating(PageRequest.of(page, size));
	        System.out.println(reactions.getContent());
	        
	        List<DetailedAnnouncementRating> resultList = reactions.getContent().stream()
	                .map(row -> {
	                    String announcementId = (String) row[0]; // announcementId (Object[0])
	                    double avgRating = (Double) row[1]; // avgRating (Object[1])

	                    // Fetch Announcement details by announcementId
	                    Announcement announcement = annRepo.findById(announcementId).orElse(null);
	                    if (announcement != null) {
	                    	String img = fromImgPathToBase64(announcement.getPhotos().get(0));
	                        // Map the details to the DTO
	                        return new DetailedAnnouncementRating(
	                                announcementId,
	                                announcement.getTitle(),
	                                announcement.getDescription(),
	                                announcement.getAddress(),
	                                announcement.getPriceForNight(),
	                               List.of( img), // Assuming photos are stored in a list
	                                avgRating
	                        );
	                    }
	                    return null; // If no announcement found, return null (you could handle this differently)
	                })
	                .filter(Objects::nonNull) // Ensure we don't add null values
	                .collect(Collectors.toList());

	            // Return the paginated result
	            return new PageImpl<>(resultList, PageRequest.of(page, size), reactions.getTotalElements());
	    } 
	
	@Override
	public PaginatedResponse<AnnouncementDto> getFilteredAnnouncementsByLocation(int page, int size, String location) {
	    Page<Announcement> announcements = annRepo.findByAddressContainingIgnoreCase(location, PageRequest.of(page, size));
	    List<Announcement> announcementsList =announcements.getContent().stream().map(a -> {

//			get services assoacited to announcement
			a.setServices(new ArrayList<>(serviceRepo.findByAnnouncementId(a.getId())));
			
			
			a.setHost( appUserRepository.findById(a.getHostId()).get());

			return a;
		}).collect(Collectors.toList());
	
	    List<AnnouncementDto> list= announcements.getContent().stream().map(a->{
	    	return mapperAnn.fromAnnouncement(a);
	    	
	    }).collect(Collectors.toList());
	    
	   return new PaginatedResponse<AnnouncementDto>(list, announcements.getTotalElements());
	}
	
	@Override
	public PaginatedResponse<AnnouncementDto> getFilteredAnnouncementsByLocationAndMore(int page, int size, String location, String title, Double price, Long categoryId) {
	  

	    Page<Announcement> announcements;
	   
	    if (location != null && !location.isBlank()) {
	        if (title != null || price != null || categoryId != null) {
	            // Apply all filters including location
	            announcements = annRepo.findByFilters( title,location, price, categoryId, PageRequest.of(page, size));
	        } else {
	            // If only location is present, filter by location only
	            announcements = annRepo.findByAddressContainingIgnoreCase(location, PageRequest.of(page, size));
	        }
	    } else {
	        // If location is not provided, check for other filters (title, price, categoryId)
	        if (title != null || price != null || categoryId != null) {
	            // Apply filters for title, price, or categoryId without location
	            announcements = annRepo.findByFilters(null, title, price, categoryId, PageRequest.of(page, size));
	        } else {
	            // If no filters are provided, return all announcements
	            announcements = annRepo.findAll(PageRequest.of(page, size));
	        }
	    }

        announcements = annRepo.findByFilters(location, title, price, categoryId, PageRequest.of(page, size));


	    List<AnnouncementDto> list = announcements.getContent().stream().map(a -> {
	        return mapperAnn.fromAnnouncement(a);
	    }).collect(Collectors.toList());
	    System.out.println(announcements.getContent());

	    return new PaginatedResponse<>(list, announcements.getTotalElements());
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
		if (announcementDto.getImages() != null && announcementDto.getImages().size()!=0 ) {
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
		
		
		existingAnnouncement.setHost( appUserRepository.findById(existingAnnouncement.getHostId()).get());
		
		return mapperAnn.fromAnnouncement(existingAnnouncement);
	}

	@Override
	public AnnouncementDto getAnnouncementById(String id) throws AnnouncementNotFoundException {
		Announcement announcement = annRepo.findById(id).get();
		if (announcement != null) {
			List<ServiceA> list = serviceRepo.findByAnnouncementId(id);
			announcement.setServices(list);

//			
			announcement.setHost( appUserRepository.findById(announcement.getHostId()).get());
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
					if (photoPath.startsWith("file:///")) {
						photoPath = photoPath.substring(8); // Remove "file:///"
					}
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
				if (s.getPhoto().startsWith("file:///")) {
					s.setPhoto(s.getPhoto().substring(8)) ; // Remove "file:///"
				}
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
				a.setHost( appUserRepository.findById(a.getHostId()).get());
			return mapperAnn.fromAnnouncement(a);
		}).collect(Collectors.toList());
		 
		 return new  PaginatedResponse<AnnouncementDto>(list, announcement.getTotalElements());
	}

	@Override
	public PaginatedResponse<AnnouncementDto> getAllAnnouncementsByHost(Long id,int page,int size) {
		// TODO Auto-generated method stub
		System.out.println(id);
		System.out.println(page);
		System.out.println(size);

		Page<Announcement> announcement =annRepo.findByHostId(id,PageRequest.of(page, size));
		List<Announcement> announcements =announcement.getContent().stream().map(a -> {

//			get services assoacited to announcement
			a.setServices(new ArrayList<>(serviceRepo.findByAnnouncementId(a.getId())));

			return a;
		}).collect(Collectors.toList());

//		return formated announcement (DTO) version

		 List<AnnouncementDto> list = announcements.stream().map(a -> {
		
			a.setHost( appUserRepository.findById(a.getHostId()).get());
			return mapperAnn.fromAnnouncement(a);
		}).collect(Collectors.toList());
		
		return new  PaginatedResponse<AnnouncementDto>(list, announcement.getTotalElements());

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
