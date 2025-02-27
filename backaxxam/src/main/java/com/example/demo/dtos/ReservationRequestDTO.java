package com.example.demo.dtos;

import java.time.LocalDate;

import lombok.Data;


@Data
public class ReservationRequestDTO {
	
	private String propertyId ;
	private String userId ;
	private LocalDate checkIn ; 
	private LocalDate checkOut ;
	private int numberOfGuests ;
	private int nights ; 
	private double totalPrice;
	

}
