package ma.axxam.reservation.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import ma.axxam.reservation.dtos.ReservationRequestDTO;
import ma.axxam.reservation.dtos.ReservationResponseDTO;
import ma.axxam.reservation.enums.ReservationStatus;

public interface IReservationService {
    ReservationResponseDTO createReservation(ReservationRequestDTO requestDTO);
    ReservationResponseDTO getReservationById(String id);
    ReservationResponseDTO updateReservation(String reservationId,ReservationRequestDTO requestDTO);
    void cancelReservation(String id);
	Page<ReservationResponseDTO> getAllReservations(Pageable pageable);
    Page<ReservationResponseDTO> getReservationsByUserId(String userId, Pageable pageable);
    Page<ReservationResponseDTO> getReservationsByStatus(ReservationStatus status, Pageable pageable);
    Page<ReservationResponseDTO> getReservationsByPropertyId(String propertyId, Pageable pageable);
	ReservationResponseDTO updateReservationStatus(String reservationId, ReservationStatus status);


}
