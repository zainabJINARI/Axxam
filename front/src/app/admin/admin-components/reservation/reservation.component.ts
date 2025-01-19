import { Component, OnInit } from '@angular/core';
import { ReservationResponse } from '../../../models/ReservationResponse';
import { ReservationService } from '../../../services/reservation-service/reservation.service';
import { PaginatedResponse } from '../../../models/PaginatedResponse';
import { ReservationStatus } from '../../../client/enums/ReservationStatus';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.css'],
})
export class ReservationComponent implements OnInit {
  reservations: ReservationResponse[] = [];
  reservation: any = null;
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

  getReservationById(id: string): void {
    if (!id) {
      console.error('ID invalide ou manquant.');
      return;
    }

    this.reservationService.getReservationById(id).subscribe({
      next: (response) => {
        if (response) {
          this.reservation = response;
          console.log('Réservation récupérée avec succès:', response);
        } else {
          console.warn('Aucune réservation trouvée pour cet ID:', id);
        }
      },
      error: (err: HttpErrorResponse) => {
        console.error('Erreur lors de la récupération de la réservation:', err);
        console.log('Statut HTTP:', err.status);
        console.log('URL de la requête:', err.url);
        alert(
          'Impossible de récupérer la réservation. Veuillez réessayer plus tard.'
        );
      },
    });
  }
}
