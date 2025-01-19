package com.example.demo.dtos;

import java.util.Date;
import java.util.List;

import com.example.demo.entities.Category;
import com.example.demo.entities.Reaction;
import com.example.demo.entities.ServiceA;
import com.example.demo.models.HostModel;
import com.example.demo.security.entities.AppUser;

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
public class DetailedAnnouncementDto {
	
	
	private String id;
	private String title;
	private String description;
	private String address;
	private double priceForNight;

	private Date dateCreation;
	
	private Category category;
	private AppUser host;

	
	private List<ServiceADto> services;
	private List<String> photos;
	private List<Reaction> reactions;
	
	public DetailedAnnouncementDto(String title,String description, String address,double priveForNight) {
		this.title=title;
		this.description=description;
		this.address=address;
		this.priceForNight=priceForNight;
		this.dateCreation= new Date();
		
	}

}
