import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { WorkspaceService, TaskItem, TimesheetItem } from '../services/workspace.service';

@Component({
  selector: 'app-employee-dashboard',
  standalone: true,
  templateUrl: './employee-dashboard.component.html',
  styleUrls: ['./employee-dashboard.component.css'],
  imports: [CommonModule, RouterModule, MatButtonModule, MatIconModule]
})
export class EmployeeDashboardComponent implements OnInit {
  loading = true;
  errorMessage = '';

  myTasks: TaskItem[] = [];
  myTimesheets: TimesheetItem[] = [];
  teamSize = 0;
  today = new Date();

  constructor(private router: Router, private workspace: WorkspaceService) {}

  ngOnInit(): void {
    this.workspace.snapshot().subscribe({
      next: (data) => {
        this.teamSize = Number(data.teamSize) || 0;
        this.myTasks = this.workspace.myTasks(data.tasks || []);
        this.myTimesheets = this.workspace.myTimesheets(data.timesheets || []);
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load developer workspace:', err);
        this.errorMessage = 'Could not load your workspace right now.';
        this.loading = false;
      }
    });
  }

  get employeeName(): string {
    return localStorage.getItem('name') || localStorage.getItem('email') || 'there';
  }

  get firstName(): string {
    return (this.employeeName || '').split(' ')[0] || this.employeeName;
  }

  get roleLabel(): string {
    return (localStorage.getItem('role') || 'EMPLOYEE').toUpperCase();
  }

  get assignedTasks(): number {
    return this.myTasks.length;
  }

  get completedTasks(): number {
    return this.myTasks.filter((task) => task.status === 'DONE').length;
  }

  get openTasks(): number {
    return this.myTasks.filter((task) => task.status !== 'DONE').length;
  }

  get inProgressTasks(): TaskItem[] {
    return this.myTasks.filter((task) => task.status === 'IN_PROGRESS');
  }

  get todoTasks(): TaskItem[] {
    return this.myTasks.filter((task) => task.status === 'TODO');
  }

  get completionRate(): number {
    return this.assignedTasks ? Math.round((this.completedTasks / this.assignedTasks) * 100) : 0;
  }

  get hoursLoggedToday(): number {
    const stamp = this.today.toISOString().slice(0, 10);
    return this.myTimesheets
      .filter((entry) => entry.date === stamp)
      .reduce((sum, entry) => sum + (Number(entry.hours) || 0), 0);
  }

  get hoursLoggedTotal(): number {
    return this.myTimesheets.reduce((sum, entry) => sum + (Number(entry.hours) || 0), 0);
  }

  get timesheetStatus(): string {
    if (!this.myTimesheets.length) {
      return 'NOT SUBMITTED';
    }
    return this.myTimesheets
      .slice()
      .sort((a, b) => String(b.date || '').localeCompare(String(a.date || '')))[0]
      .status || 'LOGGED';
  }

  get myProjects(): { name: string; taskCount: number; doneCount: number }[] {
    const map = new Map<string, { name: string; taskCount: number; doneCount: number }>();
    this.myTasks.forEach((task) => {
      const name = task.project?.name || 'Unassigned';
      const row = map.get(name) || { name, taskCount: 0, doneCount: 0 };
      row.taskCount += 1;
      if (task.status === 'DONE') {
        row.doneCount += 1;
      }
      map.set(name, row);
    });
    return Array.from(map.values());
  }

  get recentActivity(): TimesheetItem[] {
    return this.myTimesheets
      .slice()
      .sort((a, b) => String(b.date || '').localeCompare(String(a.date || '')))
      .slice(0, 4);
  }

  goToTasks() {
    this.router.navigate(['/my-tasks']);
  }

  goToSubmission() {
    this.router.navigate(['/timesheets/new']);
  }

  goToMyProject() {
    this.router.navigate(['/my-projects']);
  }

  goToUpdateProfile() {
    this.router.navigate(['/profile']);
  }
}
