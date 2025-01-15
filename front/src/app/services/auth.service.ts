import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl = 'http://localhost:8081/auth'; // Adjust to your API's base URL

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('password', password);

    return this.http.post<any>(`${this.baseUrl}/login`, null, { params }).pipe(
      tap((response) => {
        // Store the response (token, username, roles) in localStorage
        localStorage.setItem('authData', JSON.stringify(response));
      })
    );
  }

  getToken(): string | null {
    const authData = localStorage.getItem('authData');
    return authData ? JSON.parse(authData)['access-token'] : null;
  }

  getUsername(): string | null {
    const authData = localStorage.getItem('authData');
    return authData ? JSON.parse(authData)['username'] : null;
  }

  getRoles(): string[] | null {
    const authData = localStorage.getItem('authData');
    return authData ? JSON.parse(authData)['roles'] : null;
  }

  logout(): void {
    localStorage.removeItem('authData');
  }
}
