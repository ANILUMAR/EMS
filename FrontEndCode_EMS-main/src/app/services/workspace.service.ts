import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { forkJoin, Observable } from 'rxjs';

export interface TaskItem {
  id: number;
  title: string;
  description?: string;
  status: 'TODO' | 'IN_PROGRESS' | 'DONE' | string;
  project?: { id: number; name: string } | null;
  user?: any;
}

export interface TimesheetItem {
  id: number;
  employeeName: string;
  date: string;
  hours: number;
  project: string;
  status: string;
}

export interface ProjectItem {
  id: number;
  name: string;
  description?: string;
  user?: any;
}

export interface WorkspaceSnapshot {
  teamSize: number;
  tasks: TaskItem[];
  projects: ProjectItem[];
  timesheets: TimesheetItem[];
}

/**
 * Single read-only gateway for the screens that need live workspace numbers,
 * so every dashboard reads the same endpoints with the same auth header.
 */
@Injectable({ providedIn: 'root' })
export class WorkspaceService {
  private base = 'http://localhost:9001/api';

  constructor(private http: HttpClient) {}

  private headers(): HttpHeaders {
    return new HttpHeaders().set('Authorization', `Bearer ${localStorage.getItem('token') || ''}`);
  }

  getTeamSize(): Observable<number> {
    return this.http.get<number>(`${this.base}/auth/totalEmployees`, { headers: this.headers() });
  }

  getEmployees(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/auth/registredEmployees`, { headers: this.headers() });
  }

  getTasks(): Observable<TaskItem[]> {
    return this.http.get<TaskItem[]>(`${this.base}/tasks`, { headers: this.headers() });
  }

  getProjects(): Observable<ProjectItem[]> {
    return this.http.get<ProjectItem[]>(`${this.base}/projects`, { headers: this.headers() });
  }

  getTimesheets(): Observable<TimesheetItem[]> {
    return this.http.get<TimesheetItem[]>(`${this.base}/timesheets`, { headers: this.headers() });
  }

  snapshot(): Observable<WorkspaceSnapshot> {
    return forkJoin({
      teamSize: this.getTeamSize(),
      tasks: this.getTasks(),
      projects: this.getProjects(),
      timesheets: this.getTimesheets()
    });
  }

  /** Email of the signed-in user, lowercased for safe comparison. */
  currentEmail(): string {
    return (localStorage.getItem('email') || '').toLowerCase();
  }

  belongsToMe(task: TaskItem): boolean {
    const email = this.currentEmail();
    return !!email && (task.user?.email || '').toLowerCase() === email;
  }

  myTasks(tasks: TaskItem[]): TaskItem[] {
    return tasks.filter((task) => this.belongsToMe(task));
  }

  myTimesheets(timesheets: TimesheetItem[]): TimesheetItem[] {
    const email = this.currentEmail();
    const name = (localStorage.getItem('name') || '').toLowerCase();
    return timesheets.filter((entry) => {
      const owner = (entry.employeeName || '').toLowerCase();
      return owner === email || (!!name && owner === name);
    });
  }
}
