package ma.axxam.paiement.controller;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.axxam.paiement.dto.PaymentRequest;
import ma.axxam.paiement.dto.StripeResponse;
import ma.axxam.paiement.service.StripeService;

@RestController
@RequestMapping("/product/v1")


public class ProductCheckoutConroller {
	
	private StripeService stripeService ;
	
	public ProductCheckoutConroller(StripeService stripeService) {
		this.stripeService = stripeService ;
	}
	
	
	
	@PostMapping("/checkout")
	public ResponseEntity<StripeResponse> checkoutProducts(@RequestBody PaymentRequest payementRequest) {
		StripeResponse  stripeResponse =  stripeService.checkPayments(payementRequest);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(stripeResponse);
	}

}
