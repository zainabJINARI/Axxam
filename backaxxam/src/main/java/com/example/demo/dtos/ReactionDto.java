package com.example.demo.dtos;
import java.util.Date;

import com.example.demo.enums.NotificationStatus;
import com.example.demo.security.entities.AppUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ReactionDto {
	private Long idReaction;
	private int ratingValue;
	private String comment;
	@Builder.Default
	private Date postedDate= new Date();
	private Long customerId;
	private String announcementId;
	@Builder.Default
	private NotificationStatus status =NotificationStatus.UNREAD ;
	private AppUser customer;
	public ReactionDto(int ratingValue, String comment, Long customerId, String announcementId) {
		super();
		this.ratingValue = ratingValue;
		this.comment = comment;
		this.postedDate = new Date();
		this.customerId = customerId;
		this.announcementId = announcementId;
		this.status = NotificationStatus.UNREAD;
		this.customer = new AppUser();
	}

}
