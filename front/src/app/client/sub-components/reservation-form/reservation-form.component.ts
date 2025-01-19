import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { StripeResponse } from '../../../models/StripeResponse';
import { PaymentRequest } from '../../../models/PaymentRequest';
import { PaymentService } from '../../../services/payment-service/payment.service';
import { ReservationService } from '../../../services/reservation-service/reservation.service';
import { PayementStatus } from '../../enums/PaymentStatus';
import { ReservationRequest } from '../../../models/ReservationRequest';

@Component({
  selector: 'app-reservation-form',
  templateUrl: './reservation-form.component.html',
  styleUrl: './reservation-form.component.css',
})
export class ReservationFormComponent implements OnInit {
  @Input() announcement!: any;
  public reservationForm!: FormGroup;
  public totalAmount: number = 0;
  public taxRate: number = 0.1; // 10% de taxe
  public priceForNight = 0;
  public baseAmount = 0;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private payService: PaymentService,
    private resService: ReservationService
  ) {}

  ngOnInit(): void {
    this.reservationForm = this.fb.group({
      checkIn: ['', Validators.required],
      checkOut: ['', [Validators.required]],
      numberOfGuests: ['', Validators.required],
      nights: ['1', Validators.required],
    });
    const announcementId = this.announcement?.id;
    console.log("ID de l'annonce:", announcementId);

    const userId = this.authService.getUsername();
    console.log('ID de User:', userId);
    // Récupérer le prix par nuit de l'annonce
    this.priceForNight = this.announcement.priceForNight;
    this.calculateTotal();

    this.reservationForm.valueChanges.subscribe(() => {
      this.calculateTotal();
    });
  }

  // Calculer le prix total
  calculateTotal(): void {
    console.log('calculateTotal called'); // Vérifier que la méthode est appelée
    const nights = this.reservationForm.get('nights')?.value;
    console.log('Price per night:', this.priceForNight);
    console.log('Number of nights:', nights);

    if (nights && nights > 0) {
      this.baseAmount = this.priceForNight * nights;
      const taxAmount = this.baseAmount * this.taxRate;
      this.totalAmount = this.baseAmount + taxAmount;
      console.log('Base amount:', this.baseAmount);
      console.log('Tax amount:', taxAmount);
      console.log('Total amount:', this.totalAmount);
    } else {
      console.log('Invalid number of nights:', nights);
      this.totalAmount = 0;
    }
  }

  reserver(): void {
    if (this.reservationForm.valid) {
      const requestData: ReservationRequest = {
        propertyId: this.announcement?.id,
        userId: this.authService.getUsername(),
        checkIn: this.reservationForm.get('checkIn')?.value,
        checkOut: this.reservationForm.get('checkOut')?.value,
        numberOfGuests: this.reservationForm.get('numberOfGuests')?.value,
        nights: this.reservationForm.get('nights')?.value,
      };

      this.createSession(requestData); // Passer requestData à createSession
    } else {
      console.log('Le formulaire est invalide');
    }
  }

  createSession(requestData: ReservationRequest) {
    const paymentReq: PaymentRequest = {
      amount: this.totalAmount,
      announceName: this.announcement.title,
    };

    this.payService.createCheckoutSession(paymentReq).subscribe({
      next: (response: StripeResponse) => {
        window.location.href = response.sessionUrl; // Redirection vers Stripe

        // Vérifier correctement le statut de la session Stripe
        if (response.status === 'success') {
          // Il faut utiliser "response" et non "StripeResponse"
          // Créer la réservation après un paiement réussi
          this.resService
            .createReservation(PayementStatus.PAID, requestData)
            .subscribe({
              next: (reservationResponse) => {
                console.log(
                  'Réservation créée avec succès:',
                  reservationResponse
                );
              },
              error: (err) => {
                console.error(
                  'Erreur lors de la création de la réservation:',
                  err
                );
              },
            });
        } else {
          console.error('Échec du paiement Stripe');
        }
      },
      error: (err) => {
        console.error('Erreur lors de la création de la session:', err);
      },
    });
  }
}
