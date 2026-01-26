import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {
  private baseUrl = `${environment.apiUrl}/registrations`;

  constructor(private http: HttpClient) {}

  registerForEvent(eventId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/event/${eventId}`, {});
  }

  getMyRegistrations(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get(`${this.baseUrl}/my-registrations?page=${page}&size=${size}`);
  }

  cancelRegistration(registrationId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/${registrationId}/cancel`, {});
  }

  getEventAttendees(eventId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/event/${eventId}/attendees`);
  }
}