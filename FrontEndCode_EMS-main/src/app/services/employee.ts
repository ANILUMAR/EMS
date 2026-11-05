// src/app/services/employee.service.ts
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Employee } from '../models/employee';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private employees: Employee[] = [
    { id: 1, name: 'Anil kumar ', designation: 'Software Engineer', address: 'IT', email: 'john@example.com', phoneNumber: '1234567890',role:'Admin' ,registerImage:''},
    { id: 2, name: 'Deepika', designation: 'HR Manager', address: 'HR', email: 'jane@example.com', phoneNumber: '9876543210',role:'Admin',registerImage:''},
    // Add more employees here
  ];

  getEmployees(): Observable<Employee[]> {
    return of(this.employees);
  }
}
export type { Employee };

