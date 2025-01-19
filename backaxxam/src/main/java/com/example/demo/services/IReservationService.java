package com.example.demo.services;


import org.springframework.data.domain.Pageable;
import com.example.demo.dtos.PaginatedResponse;
import com.example.demo.dtos.ReservationRequestDTO;
import com.example.demo.dtos.ReservationResponseDTO;
import com.example.demo.enums.PayementStatus;
import com.example.demo.enums.ReservationStatus;



public interface IReservationService {
	
    ReservationResponseDTO createReservation(PayementStatus payStatus ,ReservationRequestDTO requestDTO);
    ReservationResponseDTO getReservationById(String id);
    ReservationResponseDTO updateReservation(String reservationId,ReservationRequestDTO requestDTO);
    void cancelReservation(String id);
    PaginatedResponse<ReservationResponseDTO> getAllReservations(Pageable pageable);
    PaginatedResponse<ReservationResponseDTO> getReservationsByUserId(String userId, Pageable pageable);
    PaginatedResponse<ReservationResponseDTO> getReservationsByStatus(ReservationStatus status, Pageable pageable);
    PaginatedResponse<ReservationResponseDTO> getReservationsByPropertyId(String propertyId, Pageable pageable);
	ReservationResponseDTO updateReservationStatus(String reservationId, ReservationStatus status);



}
