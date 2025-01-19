package com.example.demo.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import com.example.demo.enums.PayementStatus;
import com.example.demo.enums.ReservationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    
    @Id
    private String id;
    private String propertyId;
    private String userId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int nights;
    private Double totalPrice;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    private int numberOgGuests;
    private String paymentId;
    @Enumerated(EnumType.STRING)
    private PayementStatus paymentStatus;
    private LocalDate cancellationDate;
    private Double refundAmount;

    
    
    // Méthode pour calculer le nombre de nuits
    public void calculateNights() {
        if (checkIn != null && checkOut != null) {
            this.nights = (int) ChronoUnit.DAYS.between(checkIn, checkOut);
        }
    }

    // Calcul du coût total basé sur les nuits et autres critères
    public void calculateTotalPrice(double costPerNight) {
        if (nights > 0) {
            this.totalPrice = nights * costPerNight;
        }
    }

    // Vérification de la validité des dates
    public boolean isValid() {
        return checkIn != null && checkOut != null && checkOut.isAfter(checkIn);
    }

    // Génération d'un ID unique si nécessaire
    public static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    // Méthode d'initialisation lors de la création
    public void initializeReservation() {
        if (this.id == null) {
            this.id = generateUniqueId();
        }
        if (this.checkIn == null) {
            this.checkIn = LocalDate.now();
        }
        if (this.checkOut == null) {
            this.checkOut = checkIn.plusDays(1); // Valeur par défaut, si nécessaire
        }
        if (this.nights == 0 && checkIn != null && checkOut != null) {
            calculateNights();
        }
    }
}

