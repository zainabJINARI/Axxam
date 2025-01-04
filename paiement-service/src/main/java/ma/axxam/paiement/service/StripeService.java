package ma.axxam.paiement.service;

import org.springframework.stereotype.Service;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import ma.axxam.paiement.dto.PaymentRequest;
import ma.axxam.paiement.dto.StripeResponse;

@Service
public class StripeService {

    public StripeResponse checkPayments(PaymentRequest paymentRequest) {
        Stripe.apiKey = "sk_test_51QcAZERugscrmFFyek2cyIETmNZ6eEa1A0pMmxvHmBgwfGquDVGuzoctT81DDYTrNNYAv6VNKe87CkGXVV8QfXOt00Cf4JqYaj";
        
        // Remplir la partie de création du produit Stripe avec les données de PaymentRequest
        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("USD")  // Vous pouvez ajuster la devise si nécessaire
                .setUnitAmount((long) (paymentRequest.getAmount() * 100)) // Stripe prend en charge les montants en cents
                .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(1L) // Vous pouvez définir la quantité si nécessaire
                .setPriceData(priceData)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/success") // Vous pouvez ajuster cette URL selon votre besoin
                .setCancelUrl("http://localhost:8080/cancel")  // Vous pouvez ajuster cette URL selon votre besoin
                .addLineItem(lineItem)
                .build();

        Session session = null;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            System.out.println(e.getMessage());
        }

        return StripeResponse.builder()
                .status("SUCCESS")
                .message("Payment session created!!!")
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();
    }
}
