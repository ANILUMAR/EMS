import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { WorkspaceService, TaskItem } from '../services/workspace.service';

@Component({
  selector: 'app-tester-dashboard.component',
  standalone: true,
  imports: [CommonModule, RouterModule, MatButtonModule, MatIconModule],
  templateUrl: './tester-dashboard.component.html',
  styleUrl: './tester-dashboard.component.css'
})
export class TesterDashboardComponent implements OnInit {
  loading = true;
  errorMessage = '';

  myTasks: TaskItem[] = [];
  today = new Date();

  constructor(private router: Router, private workspace: WorkspaceService) {}

  ngOnInit(): void {
    this.workspace.getTasks().subscribe({
      next: (tasks) => {
        this.myTasks = this.workspace.myTasks(tasks || []);
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load tester queue:', err);
        this.errorMessage = 'Could not load your verification queue right now.';
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

  get pending(): number {
    return this.myTasks.filter((task) => task.status !== 'DONE').length;
  }

  get completed(): number {
    return this.myTasks.filter((task) => task.status === 'DONE').length;
  }

  get inProgress(): TaskItem[] {
    return this.myTasks.filter((task) => task.status === 'IN_PROGRESS');
  }

  get progress(): number {
    return this.myTasks.length ? Math.round((this.completed / this.myTasks.length) * 100) : 0;
  }

  goToTasks() {
    this.router.navigate(['/my-tasks']);
  }

  goToAllTasks() {
    this.router.navigate(['/tasks']);
  }

  goToProjects() {
    this.router.navigate(['/my-projects']);
  }

  goToTimesheet() {
    this.router.navigate(['/timesheets/new']);
  }
}
