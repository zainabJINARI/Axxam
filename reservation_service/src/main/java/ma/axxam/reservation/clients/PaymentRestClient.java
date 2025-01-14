package ma.axxam.reservation.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import ma.axxam.reservation.dtos.PaymentRequest;
import ma.axxam.reservation.dtos.StripeResponse;

@FeignClient(name="paiement-service", url="http://localhost:8080")
public interface PaymentRestClient {
    @PostMapping("/api/payments/create-session")
    StripeResponse createPaymentSession(@RequestBody PaymentRequest paymentRequest);
}
