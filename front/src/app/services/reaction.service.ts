import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ReactionService {
  private baseUrl = `http://localhost:8081/reactions`; // API Base URL

  constructor(private http: HttpClient, private router: Router,private authService: AuthService ) { }

  // Helper method to get headers with the token
  createReaction(reaction: FormData): Observable<number> {
    return this.http.post<number>(`${this.baseUrl}`, reaction, { 
      headers: this.getHeaders(false) // Don't set Content-Type manually for FormData
    });
  }

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  getAllUnreadReactionsByHost( page: number = 0, size: number = 5): Observable<any> {
    const url = `${this.baseUrl}/unread?page=${page}&size=${size}`;
    return this.http.get<any>(url, { headers: this.getHeaders() });
  }
  

  updateNotificationStatus( id:number): Observable<any> {
    const url = `${this.baseUrl}/read/${id}`;
  
  const token = localStorage.getItem('authData');
  const accessToken = token ? JSON.parse(token)['access-token'] : null;

  if (!accessToken) {
    console.error('No access token found');
    return throwError(() => new Error('Unauthorized'));
  }

  const headers = new HttpHeaders({
    'Authorization': `Bearer ${accessToken}`,
    'Content-Type': 'application/json'
  });

  return this.http.put<any>(url, {}, { headers });
  }

  // Modify getHeaders to handle FormData properly
  private getHeaders(isJson: boolean = true): HttpHeaders {
    const token = localStorage.getItem('authData');
    const accessToken = token ? JSON.parse(token)['access-token'] : null;
  
    let headers = new HttpHeaders().set('Authorization', `Bearer ${accessToken}`);
    if (isJson) {
      headers = headers.set('Content-Type', 'application/json'); // JSON only when needed
    }
    return headers;
  }

  // Handle errors globally
  private handleError(error: HttpErrorResponse): Observable<never> {
    if (error.status === 401) {
      // Unauthorized (token expired or invalid) → Redirect to login
      console.warn('Token expired or invalid. Redirecting to login...');
      localStorage.removeItem('authData'); // Clear the invalid token
      this.router.navigate(['/login']); // Redirect to the login page
    }
    return throwError(() => new Error('Something went wrong, please try again.'));
  }

 

  // Delete a reaction by ID
  deleteReaction(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, { headers: this.getHeaders() })
      .pipe(catchError(error => this.handleError(error)));
  }
}
