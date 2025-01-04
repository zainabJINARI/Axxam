package com.example.demo;
  
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.entities.Announcement;
import com.example.demo.entities.Category;
import com.example.demo.entities.ServiceA;
import com.example.demo.repositories.AnnouncementRepository;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ServiceaRepository;

@SpringBootApplication
public class AnnoucementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnnoucementServiceApplication.class, args);
	}
	
	
	@Bean
	CommandLineRunner commandLineRunner(AnnouncementRepository announcementRepository,CategoryRepository categoryRepository,ServiceaRepository serviceRepository) {
		return arg->{
			 
			Category category = Category.builder()
					.name("Villa")
					.build();
			category=categoryRepository.save(category);


			
		};
		
	}

}
