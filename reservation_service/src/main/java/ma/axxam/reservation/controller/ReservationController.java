package ma.axxam.reservation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ma.axxam.reservation.dtos.ReservationRequestDTO;
import ma.axxam.reservation.dtos.ReservationResponseDTO;
import ma.axxam.reservation.enums.ReservationStatus;
import ma.axxam.reservation.service.IReservationService;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final IReservationService resrvService;

    @PostMapping
    public ReservationResponseDTO createReservation(@RequestBody ReservationRequestDTO requestDTO) {
        return resrvService.createReservation(requestDTO);
    }

    @GetMapping("")
    public Page<ReservationResponseDTO> getAllReservations(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "5") int size) {

        // PageRequest commence à partir de 0, donc on soustrait 1 de la page
        Pageable pageable = PageRequest.of(page - 1, size);
        return resrvService.getAllReservations(pageable);
    }

    @GetMapping("/{id}")
    public ReservationResponseDTO getReservationById(@PathVariable String id) {
        return resrvService.getReservationById(id);
    }
    
    
 // Mise à jour d'une réservation
    @PutMapping("/{reservationId}")
    public ReservationResponseDTO updateReservation(
            @PathVariable String reservationId,
            @RequestBody ReservationRequestDTO reservationRequestDTO) {
        ReservationResponseDTO updatedReservation = resrvService.updateReservation(reservationId, reservationRequestDTO);
        return updatedReservation;
    }

    @GetMapping("/user/{userId}")
    public Page<ReservationResponseDTO> getReservationsByUserId(
            @PathVariable String userId,
            Pageable pageable) {
        Page<ReservationResponseDTO> reservations = resrvService.getReservationsByUserId(userId, pageable);
        return reservations;
    }

    @GetMapping("/status/{status}")
    public Page<ReservationResponseDTO> getReservationsByStatus(
            @PathVariable ReservationStatus status,
            Pageable pageable) {
        Page<ReservationResponseDTO> reservations = resrvService.getReservationsByStatus(status, pageable);
        return reservations;
    }

    @GetMapping("/property/{propertyId}")
    public Page<ReservationResponseDTO> getReservationsByPropertyId(
            @PathVariable String propertyId,
            Pageable pageable) {
        Page<ReservationResponseDTO> reservations = resrvService.getReservationsByPropertyId(propertyId, pageable);
        return reservations;
    }
    
    
    
    @PutMapping("/{reservationId}/status")
    public ReservationResponseDTO updateReservationStatus(
            @PathVariable String reservationId, 
            @RequestParam ReservationStatus status) {

        ReservationResponseDTO updatedReservation = resrvService.updateReservationStatus(reservationId, status);
        return updatedReservation;
    }
}
