import { Component, OnInit } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatCardModule } from '@angular/material/card';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatListModule } from '@angular/material/list';
import { catchError, forkJoin, Observable, of } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatButtonModule,
    RouterModule,
    MatMenuModule,
    MatIconModule,
    MatDividerModule,
    MatCardModule,
    CommonModule,
    MatDialogModule,
    MatListModule
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {
  user: any = {};
  loading = true;
  errorMessage = '';

  teamSize = 0;
  projects: any[] = [];
  tasks: any[] = [];
  timesheets: any[] = [];

  userRole = localStorage.getItem('role') || 'ADMIN';
  today = new Date();

  constructor(
    private http: HttpClient,
    private Router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadUserProfile();
    this.loadWorkspace();
  }

  get displayName(): string {
    return this.user?.username || localStorage.getItem('name') || localStorage.getItem('email') || 'there';
  }

  get firstName(): string {
    return (this.displayName || '').split(' ')[0] || this.displayName;
  }

  get avatarUrl(): string {
    return this.user?.registerImage
      ? `http://localhost:9001/attachments/${this.user.registerImage}`
      : '';
  }

  get initials(): string {
    return (this.displayName || 'U')
      .split(' ')
      .filter((part: string) => !!part)
      .slice(0, 2)
      .map((part: string) => part.charAt(0).toUpperCase())
      .join('');
  }

  get projectCount(): number { return this.projects.length; }
  get openTaskCount(): number { return this.tasks.filter((t) => t.status !== 'DONE').length; }
  get doneTaskCount(): number { return this.tasks.filter((t) => t.status === 'DONE').length; }
  get inProgressCount(): number { return this.tasks.filter((t) => t.status === 'IN_PROGRESS').length; }
  get todoCount(): number { return this.tasks.filter((t) => t.status === 'TODO').length; }
  get totalHours(): number {
    return this.timesheets.reduce((sum, entry) => sum + (Number(entry.hours) || 0), 0);
  }
  get completionRate(): number {
    return this.tasks.length ? Math.round((this.doneTaskCount / this.tasks.length) * 100) : 0;
  }

  get statusBreakdown() {
    const total = this.tasks.length || 1;
    return [
      { key: 'TODO', label: 'To do', count: this.todoCount, percent: (this.todoCount / total) * 100, tone: 'neutral' },
      { key: 'IN_PROGRESS', label: 'In progress', count: this.inProgressCount, percent: (this.inProgressCount / total) * 100, tone: 'info' },
      { key: 'DONE', label: 'Done', count: this.doneTaskCount, percent: (this.doneTaskCount / total) * 100, tone: 'success' }
    ];
  }

  get projectRows(): any[] {
    return this.projects.slice(0, 5).map((project) => {
      const projectTasks = this.tasks.filter((task) => task.project?.id === project.id);
      const done = projectTasks.filter((task) => task.status === 'DONE').length;
      return {
        ...project,
        taskCount: projectTasks.length,
        doneCount: done,
        progress: projectTasks.length ? Math.round((done / projectTasks.length) * 100) : 0
      };
    });
  }

  get recentTimesheets(): any[] {
    return [...this.timesheets]
      .sort((a, b) => String(b.date || '').localeCompare(String(a.date || '')))
      .slice(0, 5);
  }

  openHelpDialog(templateRef: any) {
    this.dialog.open(templateRef, { width: '560px', maxWidth: '92vw' });
  }

  private authHeaders(): HttpHeaders {
    return new HttpHeaders().set('Authorization', `Bearer ${localStorage.getItem('token')}`);
  }

  loadUserProfile(): void {
    const username = localStorage.getItem('email');
    if (!username) {
      return;
    }

    this.http.get(
      `http://localhost:9001/api/UserProfile/userdetails?username=${encodeURIComponent(username)}`,
      { headers: this.authHeaders() }
    ).subscribe({
      next: (data: any) => { this.user = data; },
      error: (err) => console.error('Failed to load profile:', err)
    });
  }

  loadWorkspace(): void {
    this.loading = true;
    this.errorMessage = '';
    let degraded = false;

    // Each source degrades on its own so one failing endpoint never blanks the
    // whole board (previously a single error zeroed every counter).
    const guarded = <T>(source: Observable<T>, fallback: T, label: string): Observable<T> =>
      source.pipe(
        catchError((err: any) => {
          console.error(`Failed to load ${label}:`, err);
          degraded = true;
          return of(fallback);
        })
      );

    forkJoin({
      team: guarded<number>(this.http.get<number>('http://localhost:9001/api/auth/totalEmployees', { headers: this.authHeaders() }), 0, 'team size'),
      projects: guarded<any[]>(this.http.get<any[]>('http://localhost:9001/api/projects', { headers: this.authHeaders() }), [], 'projects'),
      tasks: guarded<any[]>(this.http.get<any[]>('http://localhost:9001/api/tasks', { headers: this.authHeaders() }), [], 'tasks'),
      timesheets: guarded<any[]>(this.http.get<any[]>('http://localhost:9001/api/timesheets', { headers: this.authHeaders() }), [], 'timesheets')
    }).subscribe({
      next: ({ team, projects, tasks, timesheets }) => {
        this.teamSize = Number(team) || 0;
        this.projects = projects || [];
        this.tasks = tasks || [];
        this.timesheets = timesheets || [];
        this.errorMessage = degraded ? 'Some dashboard data could not be loaded. Check that the API is running.' : '';
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load dashboard data:', err);
        this.errorMessage = 'Some dashboard data could not be loaded.';
        this.loading = false;
      }
    });
  }

  onLogout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('email');
    localStorage.removeItem('role');
    localStorage.removeItem('name');
    this.Router.navigate(['/login']);
  }
}
