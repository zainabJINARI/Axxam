import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

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

  signup(
    username: string,
    email: string,
    password: string,
    role: string
  ): Observable<any> {
    const body = { username, email, password, role };

    return this.http.post<any>(`${this.baseUrl}/signup`, body).pipe(
      tap((response) => {
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

  getEmail(): string | null {
    const authData = localStorage.getItem('email');
    return authData ? JSON.parse(authData)['email'] : null;
  }

  getRoles(): string[] | null {
    const authData = localStorage.getItem('authData');
    return authData ? JSON.parse(authData)['roles'] : null;
  }

  // // Méthode de mise à jour de l'utilisateur
  // updateUser(
  //   username: string,
  //   email: string,
  //   password: string,
  // ): Observable<any> {
  //   const body = { username, email, password };

  //   return this.http.post<any>(`${this.baseUrl}/update/${username}`, body).pipe(
  //     tap((response) => {
  //       localStorage.setItem('authData', JSON.stringify(response));
  //     })
  //   );
  // }

  // Méthode de déconnexion
  logout(): void {
    localStorage.removeItem('authData');
    this.http.post(`${this.baseUrl}/logout`, {}).subscribe();
  }

  // Get User By his UserName
  getUserByUserName(username: string): Observable<any> {
    const headers = {
      Authorization: `Bearer ${this.getToken()}`,
    };

    return this.http
      .get<any>(`${this.baseUrl}/user/${username}`, { headers })
      .pipe(
        catchError((error) => {
          if (error.status === 401) {
            console.error(
              'Erreur 401 : Utilisateur non authentifié. Redirection...'
            );
          }
          return throwError(() => error);
        })
      );
  }

  updateUser(
    username: string,
    email?: string,
    password?: string
  ): Observable<any> {
    const updateData: { email?: string; password?: string } = {};

    if (email) {
      updateData.email = email;
    }

    if (password) {
      updateData.password = password;
    }

    const body = updateData;

    const headers = {
      Authorization: `Bearer ${this.getToken()}`,
    };

    return this.http
      .post<any>(`${this.baseUrl}/update/${username}`, body, { headers })
      .pipe(
        catchError((error) => {
          console.error(
            "Erreur lors de la mise à jour de l'utilisateur",
            error
          );
          return throwError(() => error);
        })
      );
  }
}
