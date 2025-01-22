export interface ReservationRequest {
  propertyId: string;
  userId: string | null;
  checkIn: string;
  checkOut: string;
  numberOfGuests: number;
  nights: number;
  totalPrice: number;
}
