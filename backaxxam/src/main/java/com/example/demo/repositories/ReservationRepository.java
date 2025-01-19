package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.Reservation;
import com.example.demo.enums.ReservationStatus;


public interface ReservationRepository extends JpaRepository<Reservation,String>{
	Page findAll(Pageable pageable);
    Page<Reservation> findByUserId(String userId, Pageable pageable);
    Page<Reservation> findByStatus(ReservationStatus status, Pageable pageable);
	Page<Reservation> findByPropertyId(String propertyId, Pageable pageable);
	public Page<Reservation> findByPropertyIdIn(List<String> propertyIds, Pageable pageable);



}
