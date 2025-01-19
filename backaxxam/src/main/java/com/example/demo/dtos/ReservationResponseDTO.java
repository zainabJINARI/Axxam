package com.example.demo.dtos;

import java.time.LocalDate;

import com.example.demo.enums.PayementStatus;
import com.example.demo.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationResponseDTO {

    private String id;
    private String propertyId;
    private String userId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int numberOfGuests;
    private int nights;
    private Double totalPrice;
    private ReservationStatus status;
    private PayementStatus payementStatus;  
}
