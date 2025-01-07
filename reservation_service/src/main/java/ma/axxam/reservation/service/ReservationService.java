package ma.axxam.reservation.service;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import ma.axxam.reservation.clients.PaymentRestClient;
import ma.axxam.reservation.dtos.PaymentRequest;
import ma.axxam.reservation.dtos.ReservationRequestDTO;
import ma.axxam.reservation.dtos.ReservationResponseDTO;
import ma.axxam.reservation.dtos.StripeResponse;
import ma.axxam.reservation.entities.Reservation;
import ma.axxam.reservation.enums.PayementStatus;
import ma.axxam.reservation.enums.ReservationStatus;
import ma.axxam.reservation.repository.ReservationRepository;



@Service
@RequiredArgsConstructor
public class ReservationService implements IReservationService {

	private final ReservationRepository reservRepository ;
    private final PaymentRestClient paymentClient;

	
    @Override
    public ReservationResponseDTO createReservation(ReservationRequestDTO requestDTO) {
        // Étape 1: Création de la réservation
        Reservation reservation = Reservation.builder()
                    .userId(requestDTO.getUserId())
                    .propertyId(requestDTO.getPropertyId())
                    .checkIn(requestDTO.getCheckIn())
                    .checkOut(requestDTO.getCheckOut())
                    .numberOgGuests(requestDTO.getNumberOfGuests())
                    .status(ReservationStatus.PENDING)
                    .build();

        // Étape 2: Initialisation de la réservation (générer l'ID, calcul des nuits, etc.)
        reservation.initializeReservation();
        
        // Étape 3: Calcul du prix total de la réservation (100 étant le prix par nuit, par exemple)
        reservation.calculateTotalPrice(100);
        
        // Étape 4: Vérification de la validité des dates
        if (!reservation.isValid()) {
            throw new RuntimeException("Les dates de réservation sont invalides");
        }

        // Sauvegarde initiale de la réservation dans la base de données
        reservation = reservRepository.save(reservation);

        // Étape 5: Appel du service de paiement pour initier le paiement (via Feign)
        PaymentRequest payementRequest = new PaymentRequest( reservation.getTotalPrice() , reservation.getPropertyId() );
        StripeResponse paymentResponse = paymentClient.createPaymentSession(payementRequest );

        // Vérification de la réponse de paiement (status)
        if ("success".equals(paymentResponse.getStatus())) {
            // Le paiement a été effectué avec succès
            reservation.setStatus(ReservationStatus.CONFIRMED); // Mettre la réservation à l'état confirmé
            reservation.setPaymentStatus(PayementStatus.PAID); // Mettre à jour le statut du paiement
        } else {
            // Le paiement a échoué ou est en attente
            reservation.setStatus(ReservationStatus.CANCELLED); // Annuler la réservation
            reservation.setPaymentStatus(PayementStatus.FAILED); // Mettre à jour le statut du paiement
            reservation.setRefundAmount(reservation.getTotalPrice()); // Le montant à rembourser en cas de paiement échoué
        }

        // Sauvegarde de la réservation mise à jour après paiement
        reservRepository.save(reservation);

        // Étape 6: Retourner le DTO de la réservation
        return mapToResponseDTO(reservation);
    }


	@Override
	public ReservationResponseDTO getReservationById(String id) {
	    Reservation reservation = reservRepository.findById(id).orElseThrow(()-> new RuntimeException("Reservation not found"));
		
	    return mapToResponseDTO(reservation);
	}

	@Override
	public void cancelReservation(String id) {
		Reservation reservation = reservRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
		
		LocalDate today = LocalDate.now() ;
		long daysUntilCheckIn = ChronoUnit.DAYS.between(today,reservation.getCheckIn());
		
		double  refundAmount = 0; 
		
		if(daysUntilCheckIn > 7) {
			refundAmount = reservation.getTotalPrice();
		}else if (daysUntilCheckIn > 0){
			refundAmount = reservation.getTotalPrice() * 0.5;
		} else {
			 refundAmount = 0;
		}
		
		reservation.setStatus(ReservationStatus.CANCELLED);
		reservation.setRefundAmount(refundAmount);
		reservRepository.save(reservation);
		
	}
	


	@Override
	public Page<ReservationResponseDTO> getAllReservations(Pageable pageable) {
	    return reservRepository.findAll(pageable);
	}

	@Override
	public ReservationResponseDTO updateReservation(String reservationId, ReservationRequestDTO requestDTO) {
		
		
		Reservation existingsReservation = reservRepository.findById(reservationId) .orElseThrow(() -> new RuntimeException("Reservation not found"));
		
		existingsReservation.setCheckIn(requestDTO.getCheckIn());
		existingsReservation.setCheckOut(requestDTO.getCheckOut());
		existingsReservation.setNumberOgGuests(requestDTO.getNumberOfGuests());
		existingsReservation.calculateNights();
		existingsReservation.calculateTotalPrice(100);
        Reservation updatedReservation = reservRepository.save(existingsReservation);

       return mapToResponseDTO(updatedReservation);
	}

	 @Override
	    public Page<ReservationResponseDTO> getReservationsByUserId(String userId, Pageable pageable) {
	        Page<Reservation> reservations = reservRepository.findByUserId(userId, pageable);
	        return reservations.map(this::mapToResponseDTO);
	    }

	    @Override
	    public Page<ReservationResponseDTO> getReservationsByStatus(ReservationStatus status, Pageable pageable) {
	        Page<Reservation> reservations = reservRepository.findByStatus(status, pageable);
	        return reservations.map(this::mapToResponseDTO);
	    }

	    @Override
	    public Page<ReservationResponseDTO> getReservationsByPropertyId(String propertyId, Pageable pageable) {
	        Page<Reservation> reservations = reservRepository.findByPropertyId(propertyId, pageable);
	        return reservations.map(this::mapToResponseDTO);
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
		    return ReservationResponseDTO.builder()
		            .id(reservation.getId())
		            .propertyId(reservation.getPropertyId())
		            .userId(reservation.getUserId())
		            .checkIn(reservation.getCheckIn())
		            .checkOut(reservation.getCheckOut())
		            .nights(reservation.getNights())
		            .totalPrice(reservation.getTotalPrice())
		            .numberOfGuests(reservation.getNumberOgGuests())
		            .status(reservation.getStatus()) 
		            .build();
		}



}
