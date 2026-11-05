import { Injectable } from '@angular/core';
import { of } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ProjectService {
  getProjectOverview() {
    return of({
      name: 'Employee Tracking Tool',
      client: 'IITM',
      status: 'Active',
      startDate: '2025-05-01',
      endDate: '2025-07-30',
      team: [
        { name: 'Anil Kumar' },
        { name: 'Sneha Roy' },
        { name: 'Arjun S' }
      ],
      tasks: [
        { title: 'Setup backend API', status: 'Completed' },
        { title: 'Design UI components', status: 'In Progress' },
        { title: 'Integrate authentication', status: 'Not Started' }
      ]
    });
  }
}
