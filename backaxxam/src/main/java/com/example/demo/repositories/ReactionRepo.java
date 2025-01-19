package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.dtos.AnnouncementsIdByAvg;
import com.example.demo.entities.Reaction;
import com.example.demo.enums.NotificationStatus;

public interface ReactionRepo extends JpaRepository<Reaction,Long> {
	Page<Reaction> findByAnnouncementId(String announcementId,PageRequest pageRequest);
	List<Reaction> findByAnnouncementId(String announcementId);
	Page<Reaction> findByCustomerId(Long customerId,PageRequest pageRequest);
	Page<Reaction> findByStatus(NotificationStatus status, PageRequest pageRequest);
	@Query("SELECT r.announcementId AS announcementId, AVG(r.ratingValue) AS avgRating " +
	           "FROM Reaction r " +
	           "GROUP BY r.announcementId " +
	           "ORDER BY avgRating DESC")
	    Page<Object[]> findAnnouncementIdsOrderedByAvgRating(PageRequest pageRequest);

}
