import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { Announcement } from '../models/Announcement';
import { Service } from '../models/Service';

@Injectable({
  providedIn: 'root',
})
export class AnnouncementService {
  private baseUrl = `http://localhost:8081/announcements`; // Base URL for the API

  announcement!:any[]

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  public setAnnouncements(data:any){
    this.announcement=data


  }
  

  getAnnouncementOrderByRating(page:number=0,size:number=5):Observable<any>{
    let params = new HttpParams()
    .set('page', page.toString())
    .set('size', size.toString());
    return this.http.get<any>(`${this.baseUrl}/ordered-by-rating`, { headers: this.getAuthHeaders(), params });


  }

  getAnnouncementsByHost(page: number = 0, size: number = 5): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    

    return this.http.get<any>(`${this.baseUrl}/host`, { headers: this.getAuthHeaders(), params });
  }

  getAnnouncements(page: number = 0, size: number = 5, location?: string): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (location) {
      params = params.set('location', location);
    }
    return this.http.get<any>(this.baseUrl, { headers: this.getAuthHeaders(), params });
  }

  createService(serviceFormData: FormData): Observable<any> {
    return this.http.post<Service>(`${this.baseUrl}/services`, serviceFormData, { headers: this.getAuthHeaders()});  
  }

  updateService(serviceFormData: FormData): Observable<any> {
    return this.http.put<Service>(`${this.baseUrl}/services`, serviceFormData, { headers: this.getAuthHeaders()});  
  }

  getAnnouncementById(id: string): Observable<Announcement> {
    return this.http.get<Announcement>(`${this.baseUrl}/${id}`, { headers: this.getAuthHeaders() });
  }

  createAnnouncement(announcement: FormData): Observable<string> {
    return this.http.post<string>(this.baseUrl, announcement, { headers: this.getAuthHeaders() });
  }

  updateAnnouncement(announcement: FormData): Observable<Announcement> {
    return this.http.put<Announcement>(this.baseUrl, announcement, { headers: this.getAuthHeaders() });
  }


  deleteAnnouncement(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, { headers: this.getAuthHeaders() });
  }

  deleteService(id:number):Observable<void>{
    return this.http.delete<void>(`${this.baseUrl}/services/${id}`, { headers: this.getAuthHeaders() });

  }
}
