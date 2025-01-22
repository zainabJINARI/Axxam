package com.example.demo.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.demo.dtos.PaginatedResponse;
import com.example.demo.dtos.PaymentRequest;
import com.example.demo.dtos.ReservationRequestDTO;
import com.example.demo.dtos.ReservationResponseDTO;
import com.example.demo.dtos.StripeResponse;
import com.example.demo.entities.Announcement;
import com.example.demo.entities.Reservation;
import com.example.demo.enums.PayementStatus;
import com.example.demo.enums.ReservationStatus;
import com.example.demo.repositories.AnnouncementRepository;
import com.example.demo.repositories.ReservationRepository;
import com.example.demo.security.entities.AppUser;
import com.example.demo.security.services.UserService;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class ReservationService implements IReservationService {

	private final ReservationRepository reservRepository;
    private final  UserService userService; 
    private final AnnouncementRepository announcementRepository;
	private final StripeService stripeService;

	@Override
	public ReservationResponseDTO createReservation( PayementStatus payStatus , ReservationRequestDTO requestDTO) {
		
		// Étape 1: Création de la réservation
		
		Reservation reservation = Reservation.builder().userId(requestDTO.getUserId())
				.propertyId(requestDTO.getPropertyId()).checkIn(requestDTO.getCheckIn())
				.checkOut(requestDTO.getCheckOut()).numberOgGuests(requestDTO.getNumberOfGuests())
				.status(ReservationStatus.PENDING)
				.totalPrice(requestDTO.getTotalPrice()).build();

		// Étape 2: Initialisation de la réservation (générer l'ID, calcul des nuits,
		// etc.)
		reservation.initializeReservation();

		// Étape 3: Calcul du prix total de la réservation (100 étant le prix par nuit,
		// par exemple)

		// Étape 4: Vérification de la validité des dates
		if (!reservation.isValid()) {
			throw new RuntimeException("Les dates de réservation sont invalides");
		}

		// Sauvegarde initiale de la réservation dans la base de données
		reservation = reservRepository.save(reservation);


		// Vérification de la réponse de paiement (status)
		if (PayementStatus.PAID.equals(payStatus)) {
			// Le paiement a été effectué avec succès
			reservation.setStatus(ReservationStatus.CONFIRMED); // Mettre la réservation à l'état confirmé
			reservation.setPaymentStatus(PayementStatus.PAID) ;
			// Sauvegarde de la réservation mise à jour après paiement
			reservRepository.save(reservation);// Mettre à jour le statut du paiement
		} else {
			// Le paiement a échoué ou est en attente
			reservation.setStatus(ReservationStatus.CANCELLED); // Annuler la réservation
			reservation.setPaymentStatus(PayementStatus.FAILED); // Mettre à jour le statut du paiement
			reservation.setRefundAmount(reservation.getTotalPrice()); // Le montant à rembourser en cas de paiement
																		// échoué
		}
		// Étape 6: Retourner le DTO de la réservation
		return mapToResponseDTO(reservation);
	}

//
	@Override
	public ReservationResponseDTO getReservationById(String id) {
		Reservation reservation = reservRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Reservation not found"));
		return mapToResponseDTO(reservation);
	}

	@Override
	public void cancelReservation(String id) {
		Reservation reservation = reservRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Reservation not found"));

		LocalDate today = LocalDate.now();
		long daysUntilCheckIn = ChronoUnit.DAYS.between(today, reservation.getCheckIn());

		double refundAmount = 0;

		if (daysUntilCheckIn > 7) {
			refundAmount = reservation.getTotalPrice();
		} else if (daysUntilCheckIn > 0) {
			refundAmount = reservation.getTotalPrice() * 0.5;
		} else {
			refundAmount = 0;
		}

		reservation.setStatus(ReservationStatus.CANCELLED);
		reservation.setRefundAmount(refundAmount);
		reservRepository.save(reservation);

	}


	@Override
	public PaginatedResponse<ReservationResponseDTO> getAllReservations(Pageable pageable) {
	    // Récupérer l'utilisateur actuel (vous devez avoir une méthode ou un service pour cela)
	    AppUser currentUser = userService.getCurrentUser(); // Méthode hypothétique pour récupérer l'utilisateur actuel

	    // Récupérer toutes les annonces de cet utilisateur
	    List<Announcement> announcementsByCurrentUser = announcementRepository.findByHostId(currentUser.getId());

	    // Récupérer les ID des annonces créées par l'utilisateur actuel
	    List<String> announcementIds = announcementsByCurrentUser.stream()
	        .map(Announcement::getId)
	        .collect(Collectors.toList());

	    // Récupérer les réservations associées aux annonces créées par l'utilisateur actuel
	    Page<Reservation> list = reservRepository.findByPropertyIdIn(announcementIds, pageable);

	    // Mapper les réservations en DTO
	    List<ReservationResponseDTO> reservationResponseDTOs = list.getContent().stream()
	        .map(reservation -> mapToResponseDTO(reservation))
	        .collect(Collectors.toList());

	    // Retourner la réponse paginée
	    return new PaginatedResponse<ReservationResponseDTO>(reservationResponseDTOs, list.getTotalElements());
	}


	
	
	@Override
	public ReservationResponseDTO updateReservation(String reservationId, ReservationRequestDTO requestDTO) {

		Reservation existingsReservation = reservRepository.findById(reservationId)
				.orElseThrow(() -> new RuntimeException("Reservation not found"));

		existingsReservation.setCheckIn(requestDTO.getCheckIn());
		existingsReservation.setCheckOut(requestDTO.getCheckOut());
		existingsReservation.setNumberOgGuests(requestDTO.getNumberOfGuests());
		existingsReservation.calculateNights();
		existingsReservation.calculateTotalPrice(100);
		Reservation updatedReservation = reservRepository.save(existingsReservation);

		return mapToResponseDTO(updatedReservation);
	}

	@Override
	public PaginatedResponse<ReservationResponseDTO> getReservationsByUserId(String userId, Pageable pageable) {
		Page<Reservation> reservations = reservRepository.findByUserId(userId, pageable);
		// Transformer les entités en DTOs et créer un PaginatedResponse
		List<ReservationResponseDTO> reservationResponseDTOs = reservations.getContent().stream()
				.map(this::mapToResponseDTO) // Transformation avec mapToResponseDTO
				.collect(Collectors.toList());

		return new PaginatedResponse<>(reservationResponseDTOs, reservations.getTotalElements());
	}

	@Override
	public PaginatedResponse<ReservationResponseDTO> getReservationsByStatus(ReservationStatus status,
			Pageable pageable) {
		Page<Reservation> reservations = reservRepository.findByStatus(status, pageable);
		// Transformer les entités en DTOs et créer un PaginatedResponse
		List<ReservationResponseDTO> reservationResponseDTOs = reservations.getContent().stream()
				.map(this::mapToResponseDTO) // Transformation avec mapToResponseDTO
				.collect(Collectors.toList());

		return new PaginatedResponse<>(reservationResponseDTOs, reservations.getTotalElements());
	}

	@Override
	public PaginatedResponse<ReservationResponseDTO> getReservationsByPropertyId(String propertyId, Pageable pageable) {
		Page<Reservation> reservations = reservRepository.findByPropertyId(propertyId, pageable);
		// Transformer les entités en DTOs et créer un PaginatedResponse
		List<ReservationResponseDTO> reservationResponseDTOs = reservations.getContent().stream()
				.map(this::mapToResponseDTO) // Transformation avec mapToResponseDTO
				.collect(Collectors.toList());

		return new PaginatedResponse<>(reservationResponseDTOs, reservations.getTotalElements());
	}

	@Override
	public ReservationResponseDTO updateReservationStatus(String reservationId, ReservationStatus status) {
		Reservation reservation = reservRepository.findById(reservationId)
				.orElseThrow(() -> new RuntimeException("Reservation not found"));

		reservation.setStatus(status);

		Reservation updatedReservation = reservRepository.save(reservation);

		return mapToResponseDTO(updatedReservation);
	}

	private ReservationResponseDTO mapToResponseDTO(Reservation reservation) {
		return ReservationResponseDTO.builder().id(reservation.getId()).propertyId(reservation.getPropertyId())
				.userId(reservation.getUserId()).checkIn(reservation.getCheckIn()).checkOut(reservation.getCheckOut())
				.nights(reservation.getNights()).totalPrice(reservation.getTotalPrice())
				.numberOfGuests(reservation.getNumberOgGuests()).status(reservation.getStatus()).build();
	}

	


}
