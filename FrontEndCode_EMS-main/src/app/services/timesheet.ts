import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TimesheetEntry } from '../models/employee';

@Injectable({
  providedIn: 'root'
})
export class Timesheet {
  private apiUrl = 'http://localhost:9001/api/timesheets';

  constructor(private http: HttpClient) {}

  private headers(): HttpHeaders {
    return new HttpHeaders().set('Authorization', `Bearer ${localStorage.getItem('token') || ''}`);
  }

  getTimesheets(): Observable<TimesheetEntry[]> {
    return this.http.get<TimesheetEntry[]>(this.apiUrl, { headers: this.headers() });
  }

  saveTimesheet(entry: TimesheetEntry): Observable<TimesheetEntry> {
    return this.http.post<TimesheetEntry>(this.apiUrl, entry, { headers: this.headers() });
  }
}
