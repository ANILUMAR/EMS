import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:9001/api/auth/users'; 

  isauthenticated()
  {
     return !!localStorage.getItem('token');
  }

  updateUser(id: number, userData: any, file?: File): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    const userPayload = {
      id: userData.id,
      email: userData.email,
      name: userData.name,
      role: userData.role || 'EMPLOYEE',
      address: userData.address,
      phoneNumber: userData.phoneNumber,
      lowerName: userData.lowerName || '',
      registerImage: userData.registerImage || '',
    };

    const formData = new FormData();
    formData.append('user', new Blob([JSON.stringify(userPayload)], { type: 'application/json' }));

    if (file) {
      formData.append('file', file);
    }

    return this.http.put(`${this.apiUrl}/${id}`, formData, { headers });
  }


   constructor(private http: HttpClient) { }
}
