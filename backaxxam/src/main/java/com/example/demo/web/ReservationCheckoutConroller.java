package com.example.demo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.PaymentRequest;
import com.example.demo.dtos.StripeResponse;
import com.example.demo.services.StripeService;


@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:4200")
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
