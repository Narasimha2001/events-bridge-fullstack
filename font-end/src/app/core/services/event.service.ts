import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { EventSearchFilter } from '../models/event.model';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private baseUrl = `${environment.apiUrl}/event`;

  constructor(private http: HttpClient) { }

  createEvent(eventData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/create`, eventData);
  }

  getEventsByClub(clubId: number, page: number = 0, size: number = 10): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get(`${this.baseUrl}/${clubId}/all`, { params });
  }

  searchEvents(filter: EventSearchFilter, page: number = 0, size: number = 10): Observable<any> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);

    if (filter.title) params = params.set('title', filter.title);
    if (filter.status) params = params.set('status', filter.status);
    if (filter.clubId) params = params.set('clubId', filter.clubId);

    if (filter.startDate) params = params.set('startDate', filter.startDate);
    if (filter.endDate) params = params.set('endDate', filter.endDate);

    return this.http.get(`${this.baseUrl}/search`, { params });
  }

  uploadBanner(eventId: number, file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${this.baseUrl}/${eventId}/banner`, formData);
  }

  getEventById(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  updateEvent(eventData: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/update`, eventData);
  }

  deleteEvent(eventId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${eventId}`);
  }
}