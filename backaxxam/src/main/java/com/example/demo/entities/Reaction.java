package com.example.demo.entities;


import java.util.Date;


import com.example.demo.enums.NotificationStatus;
import com.example.demo.models.CustomerModel;
import com.example.demo.security.entities.AppUser;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Reaction {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)

	private Long idReaction;
	private int ratingValue;
	private String comment;
	private Date postedDate;
	private Long customerId;
	private String announcementId;
	@Enumerated(EnumType.STRING)
	private NotificationStatus status;
	
	
	@Transient
	private Announcement announcement;
	
	@Transient
	private AppUser customer;
	

}
