import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ReservationRequest } from '../../models/ReservationRequest';
import { ReservationResponse } from '../../models/ReservationResponse';
import { Observable } from 'rxjs';
import { PaginatedResponse } from '../../models/PaginatedResponse';
import { ReservationStatus } from '../../client/enums/ReservationStatus';
import { AuthService } from '../auth.service';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  
  private apiUrl = 'http://localhost:8081/api/reservations';

  constructor(private http: HttpClient, private authService: AuthService) {}

  // Créer une réservation
  createReservation(
    requestDTO: ReservationRequest
  ): Observable<ReservationResponse> {
    return this.http.post<ReservationResponse>(this.apiUrl, requestDTO);
  }

  getAllReservations(
    page: number = 1,  // Remarque : La page commence à 1
    size: number = 5
  ): Observable<PaginatedResponse<ReservationResponse>> {
    const token = this.authService.getToken(); // Utilisez la méthode getToken() de AuthService pour obtenir le token
  
    let headers = new HttpHeaders();
  
    if (token) {
      // Si un token est disponible, ajoutez-le dans l'en-tête Authorization
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
  
    const params = new HttpParams()
      .set('page', page.toString())  // Envoi de la page sans soustraction
      .set('size', size.toString());
  
    return this.http.get<PaginatedResponse<ReservationResponse>>(this.apiUrl, {
      headers: headers,
      params: params, // Paramètres de pagination
    });
  }

  // Obtenir les réservations par statut
  getReservationsByStatus(
    status: ReservationStatus,
    page: number = 1,
    size: number = 5
  ): Observable<PaginatedResponse<ReservationResponse>> {
    // Récupérer le token via le service d'authentification
    const token = this.authService.getToken(); // Utilisez la méthode getToken() de AuthService pour obtenir le token
  
    let headers = new HttpHeaders();
    
    // Si un token est disponible, ajoutez-le dans l'en-tête Authorization
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
  
    // Définir les paramètres de pagination
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
  
    // Effectuer la requête GET avec les en-têtes et les paramètres
    return this.http.get<PaginatedResponse<ReservationResponse>>(
      `${this.apiUrl}/status/${status}`,
      { headers, params }  // Ajout des headers et des paramètres
    );
  }
  
  

  // getAllReservations(
  //   page: number = 1,
  //   size: number = 5
  // ): Observable<PaginatedResponse<ReservationResponse>> {
  //   const token = this.authService.getToken(); // Utilisez la méthode getToken() de AuthService pour obtenir le token

  //   let headers = new HttpHeaders();

  //   if (token) {
  //     // Si un token est disponible, ajoutez-le dans l'en-tête Authorization
  //     headers = headers.set('Authorization', `Bearer ${token}`);
  //   }

  //   const params = new HttpParams()
  //     .set('page', page.toString())
  //     .set('size', size.toString());

  //   return this.http.get<PaginatedResponse<ReservationResponse>>(this.apiUrl, {
  //     headers: headers, // Ajoutez les en-têtes à la requête
  //     params: params, // Ajoutez les paramètres de pagination
  //   });
  // }

  // Obtenir une réservation par ID
  getReservationById(id: string): Observable<ReservationResponse> {
    return this.http.get<ReservationResponse>(`${this.apiUrl}/${id}`);
  }

  // Mettre à jour une réservation
  updateReservation(
    reservationId: string,
    requestDTO: ReservationRequest
  ): Observable<ReservationResponse> {
    return this.http.put<ReservationResponse>(
      `${this.apiUrl}/${reservationId}`,
      requestDTO
    );
  }

  // Obtenir les réservations d'un utilisateur par ID
  getReservationsByUserId(
    userId: string,
    page: number = 1,
    size: number = 5
  ): Observable<PaginatedResponse<ReservationResponse>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PaginatedResponse<ReservationResponse>>(
      `${this.apiUrl}/user/${userId}`,
      { params }
    );
  }

  

  // Obtenir les réservations par ID de propriété
  getReservationsByPropertyId(
    propertyId: string,
    page: number = 1,
    size: number = 5
  ): Observable<PaginatedResponse<ReservationResponse>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PaginatedResponse<ReservationResponse>>(
      `${this.apiUrl}/property/${propertyId}`,
      { params }
    );
  }

  // Mettre à jour le statut d'une réservation
  updateReservationStatus(
    reservationId: string,
    status: ReservationStatus
  ): Observable<ReservationResponse> {
    const params = new HttpParams().set('status', status.toString());
    return this.http.put<ReservationResponse>(
      `${this.apiUrl}/${reservationId}/status`,
      null,
      { params }
    );
  }
}
