import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StripeResponse } from '../../models/StripeResponse';
import { PaymentRequest } from '../../models/PaymentRequest';

import { AuthService } from '../auth.service';

@Injectable({
  providedIn: 'root',
})
export class PaymentService {
  private apiUrl = 'http://localhost:8081/api/payments';

  constructor(private http: HttpClient, private authService: AuthService) {}

  createCheckoutSession(
    paymentRequest: PaymentRequest
  ): Observable<StripeResponse> {
    const token = this.authService.getToken();

    let headers = new HttpHeaders();

    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    return this.http.post<StripeResponse>(
      `${this.apiUrl}/create-session`,
      paymentRequest,
      { headers }
    );
  }
}
