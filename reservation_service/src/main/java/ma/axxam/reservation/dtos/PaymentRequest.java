package ma.axxam.reservation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
	private double amount;
	private String announceName;
}






