export interface ReservationRequest {
  propertyId: string;
  userId: string;
  checkIn: string;
  checkOut: string;
  numberOfGuests: number;
  nights: number;
}
