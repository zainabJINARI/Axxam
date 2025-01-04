package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement,String>{
	Page<Announcement> findByHostId(Long hostId, PageRequest pageRequest);
	List<Announcement> findByHostId(Long hostId);
	@Query("SELECT a FROM Announcement a WHERE LOWER(a.address) LIKE LOWER(CONCAT('%', :address, '%'))")
	Page<Announcement> findByAddressContainingIgnoreCase(@Param("address") String address,  PageRequest pageRequest);


}
