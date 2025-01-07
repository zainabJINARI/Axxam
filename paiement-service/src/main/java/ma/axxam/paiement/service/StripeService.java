package ma.axxam.paiement.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import ma.axxam.paiement.dto.PaymentRequest;
import ma.axxam.paiement.dto.StripeResponse;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    private String secretKey = "sk_test_51QcAZERugscrmFFyek2cyIETmNZ6eEa1A0pMmxvHmBgwfGquDVGuzoctT81DDYTrNNYAv6VNKe87CkGXVV8QfXOt00Cf4JqYaj";

    private String successUrl = "http://localhost:8080/success";

    private String cancelUrl = "http://localhost:8080/cancel";

    public StripeResponse createStripeSession(PaymentRequest paymentRequest) {
        Stripe.apiKey = secretKey;

        try {
            // Configure les paramètres de la session de paiement
            SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd") // Remplacez par votre devise
                                .setUnitAmount((long) (paymentRequest.getAmount() * 100)) // Convertir en cents
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(paymentRequest.getAnnounceName())
                                        .build()
                                )
                                .build()
                        )
                        .setQuantity(1L)
                        .build()
                )
                .build();

            // Crée la session de paiement
            Session session = Session.create(params);

            // Retourne la réponse Stripe
            return StripeResponse.builder()
                .status("success")
                .message("Session créée avec succès.")
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();

        } catch (StripeException e) {
            e.printStackTrace();
            return StripeResponse.builder()
                .status("error")
                .message("Échec de la création de la session Stripe: " + e.getMessage())
                .build();
        }
    }
}
