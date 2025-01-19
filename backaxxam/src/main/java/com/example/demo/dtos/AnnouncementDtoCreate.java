package com.example.demo.dtos;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.ToString;
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AnnouncementDtoCreate {
	
	
    private String id;
	private String title;
	private String description;
	private String address;
	private double priceForNight;
	private Long hostId;

	private Date dateCreation;
	
	private Long categoryId;
	private List<MultipartFile> images;
	
	
	



}
