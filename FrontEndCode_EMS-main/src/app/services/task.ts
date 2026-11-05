import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TaskModel } from '../models/employee';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private apiUrl = 'http://localhost:9001/api/assigntasks/getAssignTask';

  constructor(private http: HttpClient) {}

  getAssignTasks(): Observable<TaskModel[]> {
    const token = localStorage.getItem('token'); // or sessionStorage.getItem('token')

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<TaskModel[]>(this.apiUrl, { headers });
  }
}