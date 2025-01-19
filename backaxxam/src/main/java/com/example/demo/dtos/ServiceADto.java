package com.example.demo.dtos;


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
public class ServiceADto {
private Long id;
	
	private String title;
	private String description;
	private String image;
	private String announcementId;
	

}
