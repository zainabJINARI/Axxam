package com.example.demo.web;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dtos.PaginatedResponse;
import com.example.demo.dtos.ReservationRequestDTO;
import com.example.demo.dtos.ReservationResponseDTO;
import com.example.demo.enums.PayementStatus;
import com.example.demo.enums.ReservationStatus;
import com.example.demo.services.IReservationService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {

    private final IReservationService resrvService;

    @PostMapping
    public ReservationResponseDTO createReservation(@RequestParam PayementStatus payStatus, @RequestBody ReservationRequestDTO requestDTO) {
        return resrvService.createReservation(payStatus , requestDTO);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_HOST')")
    @GetMapping("")
    public PaginatedResponse<ReservationResponseDTO> getAllReservations(
        @RequestParam(defaultValue = "1") int page,  
        @RequestParam(defaultValue = "5") int size) { 
        Pageable pageable = PageRequest.of(page - 1, size);
        return resrvService.getAllReservations(pageable);  
    }


    
    @PreAuthorize("hasAuthority('SCOPE_ROLE_HOST')")
    @GetMapping("/{id}")
    public ReservationResponseDTO getReservationById(@PathVariable String id) {
        return resrvService.getReservationById(id);
    }
    
    
 // Mise à jour d'une réservation
    @PreAuthorize("hasAuthority('SCOPE_ROLE_HOST')")
    @PutMapping("/{reservationId}")
    public ReservationResponseDTO updateReservation(
            @PathVariable String reservationId,
            @RequestBody ReservationRequestDTO reservationRequestDTO) {
        ReservationResponseDTO updatedReservation = resrvService.updateReservation(reservationId, reservationRequestDTO);
        return updatedReservation;
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_HOST' , 'SCOPE_ROLE_USER')")
    @GetMapping("/user/{userId}")
    public PaginatedResponse<ReservationResponseDTO> getReservationsByUserId(
            @PathVariable String userId,
            @RequestParam(defaultValue = "1") int page,  
            @RequestParam(defaultValue = "5") int size) {  

        Pageable pageable = PageRequest.of(page - 1, size); 

        return resrvService.getReservationsByUserId(userId, pageable);
    }


    @PreAuthorize("hasAuthority('SCOPE_ROLE_HOST')")
    @GetMapping("/status/{status}")
    public PaginatedResponse<ReservationResponseDTO> getReservationsByStatus(
            @PathVariable ReservationStatus status,
            @RequestParam(defaultValue = "1") int page,  
            @RequestParam(defaultValue = "5") int size) { 
        Pageable pageable = PageRequest.of(page - 1, size);  

        return resrvService.getReservationsByStatus(status, pageable);
    }

    
    @PreAuthorize("hasAuthority('SCOPE_ROLE_HOST')")
    @GetMapping("/property/{propertyId}")
    public PaginatedResponse<ReservationResponseDTO> getReservationsByPropertyId(
            @PathVariable String propertyId,
            @RequestParam(defaultValue = "1") int page, 
            @RequestParam(defaultValue = "5") int size) { 

        Pageable pageable = PageRequest.of(page - 1, size); 
        return resrvService.getReservationsByPropertyId(propertyId, pageable);
    }

    
    
    @PreAuthorize("hasAuthority('SCOPE_ROLE_HOST')")
    @PutMapping("/{reservationId}/status")
    public ReservationResponseDTO updateReservationStatus(
            @PathVariable String reservationId, 
            @RequestParam ReservationStatus status) {

        ReservationResponseDTO updatedReservation = resrvService.updateReservationStatus(reservationId, status);
        return updatedReservation;
    }

   

    
    
}
