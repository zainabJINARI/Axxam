import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Category } from '../models/Category';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private baseUrl = 'http://localhost:8081/announcements'; 

  private categories:Category[]=[]

  constructor(private http: HttpClient) {}

  public getCategories():Observable<any>{
    if(this.categories.length==0){
      return this.fetchCategories()
    }else{
      return of(this.categories)
    }
    
  }


  private fetchCategories(): Observable<any> {
    const token = localStorage.getItem('authData');
    const accessToken = token ? JSON.parse(token)['access-token'] : null;
    console.log(accessToken)

    const headers = new HttpHeaders().set('Authorization', `Bearer ${accessToken}`);

    return this.http.get<any>(`${this.baseUrl}/categories`, { headers }).pipe(
      tap((response) => {
        console.log('Categories fetched: ', response);
        this.categories=response
      })
    );
  }
}
