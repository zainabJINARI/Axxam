package ma.axxam.paiement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.axxam.paiement.dto.PaymentRequest;
import ma.axxam.paiement.dto.StripeResponse;
import ma.axxam.paiement.service.StripeService;

@RestController
@RequestMapping("/api/payments")


public class ReservationCheckoutConroller {
	
	private StripeService stripeService ;
	
	public ReservationCheckoutConroller(StripeService stripeService) {
		this.stripeService = stripeService ;
	}
	
	
	
	@PostMapping("/create-session")
	public ResponseEntity<StripeResponse> checkoutProducts(@RequestBody PaymentRequest payementRequest) {
		StripeResponse stripeResponse = stripeService.createStripeSession(payementRequest);
        if ("success".equals(stripeResponse.getStatus())) {
            return ResponseEntity.ok(stripeResponse);
        } else {
            return ResponseEntity.badRequest().body(stripeResponse);
        }
	}

}
