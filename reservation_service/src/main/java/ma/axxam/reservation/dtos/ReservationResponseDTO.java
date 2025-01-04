package ma.axxam.reservation.dtos;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;
import ma.axxam.reservation.enums.ReservationStatus;


@Data
@Builder
public class ReservationResponseDTO {
	
	private String id ;
	private String propertyId ;
	private String userId ;
	private LocalDate checkIn ;
	private LocalDate checkOut ;
	private int numberOfGuests ;
	private int nights ;
	private Double totalPrice ;
	private ReservationStatus status ;
	
	

}
