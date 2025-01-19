package com.example.demo.dtos;


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
public class ServiceADtoCreate {

	private Long id;
	private String title;
	private String description;
	private MultipartFile image;
	private String announcementId;
}

