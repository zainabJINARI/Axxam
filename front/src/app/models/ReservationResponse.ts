export interface ReservationResponse {
  id: string;
  propertyId: string;
  userId: string;
  checkIn: string;
  checkOut: string;
  numberOfGuests: number;
  nights: number;
  totalPrice: number;
  status: string;
  payementStatus: string;
}
