package com.example.demo.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Reaction;
import com.example.demo.enums.NotificationStatus;

public interface ReactionRepo extends JpaRepository<Reaction,Long> {
	Page<Reaction> findByAnnouncementId(String announcementId,PageRequest pageRequest);
	Page<Reaction> findByCustomerId(Long customerId,PageRequest pageRequest);
	Page<Reaction> findByStatus(NotificationStatus status, PageRequest pageRequest);

}
