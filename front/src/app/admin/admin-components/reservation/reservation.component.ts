import { Component, OnInit } from '@angular/core';
import { ReservationResponse } from '../../../models/ReservationResponse';
import { ReservationService } from '../../../services/reservation-service/reservation.service';
import { PaginatedResponse } from '../../../models/PaginatedResponse';
import { ReservationStatus } from '../../../client/enums/ReservationStatus';

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.css'],
})
export class ReservationComponent implements OnInit {

  reservations: ReservationResponse[] = [];
  totalReservations: number = 0;
  totalPages: number = 0;
  page: number = 1;
  size: number = 5;

  constructor(private reservationService: ReservationService) {}

  ngOnInit(): void {
    this.loadReservations();
  }

  loadReservations(): void {
    this.reservationService.getAllReservations(this.page, this.size).subscribe(
      (response: any) => {
        console.log("Réponse de l'API:", response);
        console.log('Reservations:', response.items);
        console.log('Total Reservations:', response.totalItems);

        this.reservations = response.items;
        this.totalReservations = response.totalItems;
        this.totalPages = Math.ceil(this.totalReservations / this.size);
      },
      (error) => {
        console.error(
          'Erreur lors de la récupération des réservations:',
          error
        );
      }
    );
  }

  goToNextPage(): void {
    if (this.page < this.totalPages) {
      this.page++;
      this.loadReservations();
    }
  }

  goToPreviousPage(): void {
    if (this.page > 1) {
      this.page--;
      this.loadReservations();
    }
  }

  filterByStatus(reservationStatus: ReservationStatus): void {
    this.reservationService
      .getReservationsByStatus(reservationStatus, this.page, this.size)
      .subscribe(
        (response: any) => {
          console.log("Réponse de l'API:", response);
          console.log('Reservations:', response.items);
          console.log('Total Reservations:', response.totalItems);

          this.reservations = response.items;
          this.totalReservations = response.totalItems;
          this.totalPages = Math.ceil(this.totalReservations / this.size);
        },
        (error) => {
          console.error(
            'Erreur lors de la récupération des réservations:',
            error
          );
        }
      );
  }

  pendingReservation(): void {
    this.filterByStatus(ReservationStatus.PENDING);
  }

  comfirmedReservation(): void {
    this.filterByStatus(ReservationStatus.CONFIRMED);
  }

  canceledReservation(): void {
    this.filterByStatus(ReservationStatus.CANCELLED);
  }
}
