package com.example.demo.entities;

import java.util.Date;
import java.util.List;

import com.example.demo.model.HostModel;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class Announcement {
	@Id
	private String id;
	private String title;
	private String description;
	private String address;
	private double priceForNight;
	private List<String> photos;
	private Date dateCreation;
	
	@ManyToOne
	@JoinColumn(name = "idC", nullable = false)
	private Category category;
	private Long hostId;
	@Transient
	private HostModel host;
	
	@Transient
	private List<ServiceA> services;
	
	
	
	
	
	

}
