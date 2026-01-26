import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { CreateClubRequest } from '../models/club.model';

@Injectable({
  providedIn: 'root'
})
export class ClubService {
  private baseUrl = `${environment.apiUrl}/club`;

  constructor(private http: HttpClient) {}

  createClub(club: CreateClubRequest): Observable<any> {
    return this.http.post(`${this.baseUrl}/create`, club);
  }

  getAllClubs(page: number = 0, size: number = 10): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get(`${this.baseUrl}/all`, { params });
  }

  getMyClubs(): Observable<any> {
    return this.http.get(`${this.baseUrl}/my-clubs`);
  }

  deleteClub(clubId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${clubId}`);
  }
}