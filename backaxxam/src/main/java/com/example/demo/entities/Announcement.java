package com.example.demo.entities;

import java.util.Date;
import java.util.List;

import com.example.demo.models.HostModel;
import com.example.demo.security.entities.AppUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
	@Lob
	@Column(length = 10000)
	private List<String> photos;
	private Date dateCreation;
	@ManyToOne
	@JoinColumn(name = "idC", nullable = false)
	private Category category;
	private Long hostId;
	@Transient
	private AppUser host;
	@Transient
	private List<ServiceA> services;
	
}

