import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TaskModel } from '../models/employee';

@Component({
  selector: 'app-all-task-component',
  standalone: true,
  templateUrl: './all-task-component.html',
  styleUrl: './all-task-component.css',
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatTooltipModule
  ]
})
export class AllTaskComponent implements OnInit {
  tasks: TaskModel[] = [];
  loading = true;
  errorMessage = '';
  search = '';

  private apiUrl = 'http://localhost:9001/api/assigntasks/getAssignTask';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.load();
  }

  get filteredTasks(): TaskModel[] {
    const term = this.search.trim().toLowerCase();
    if (!term) return this.tasks;
    return this.tasks.filter((task) =>
      [task.taskTitle, task.project, task.assignedToName, task.module, task.screen, task.priority]
        .filter(Boolean)
        .some((value) => String(value).toLowerCase().includes(term))
    );
  }

  get overdueCount(): number {
    const today = new Date().setHours(0, 0, 0, 0);
    return this.tasks.filter((task) => task.endDate && new Date(task.endDate).getTime() < today).length;
  }

  get highPriorityCount(): number {
    return this.tasks.filter((task) => (task.priority || '').toUpperCase() === 'HIGH').length;
  }

  get projectCount(): number {
    return new Set(this.tasks.map((task) => task.project).filter(Boolean)).size;
  }

  load(): void {
    this.loading = true;
    const headers = new HttpHeaders({ Authorization: `Bearer ${localStorage.getItem('token')}` });
    const email = localStorage.getItem('email');

    this.http.get<TaskModel[]>(`${this.apiUrl}?email=${email}`, { headers }).subscribe({
      next: (tasks) => {
        this.tasks = tasks || [];
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load all tasks', err);
        this.errorMessage = 'Could not load tasks right now.';
        this.loading = false;
      }
    });
  }

  priorityBadge(priority: string): string {
    switch ((priority || '').toUpperCase()) {
      case 'HIGH': return 'ems-badge--danger';
      case 'MEDIUM': return 'ems-badge--warning';
      case 'LOW': return 'ems-badge--success';
      default: return 'ems-badge--neutral';
    }
  }

  isOverdue(task: TaskModel): boolean {
    return !!task.endDate && new Date(task.endDate).getTime() < new Date().setHours(0, 0, 0, 0);
  }

  initials(name: string): string {
    return (name || '?')
      .split(' ')
      .filter((part) => !!part)
      .slice(0, 2)
      .map((part) => part.charAt(0).toUpperCase())
      .join('');
  }

  close(task: TaskModel): void {
    const reason = prompt(`Reason for deleting "${task.taskTitle}":`);
    if (!reason) return;

    const headers = new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem('token')}`,
      'Content-Type': 'application/json'
    });

    this.http.post(
      'http://localhost:9001/api/assigntasks/delete',
      { taskId: task.id, reason, deletedBy: localStorage.getItem('email') },
      { headers, responseType: 'text' }
    ).subscribe({
      next: (res) => {
        alert(res);
        this.load();
      },
      error: (err) => {
        console.error('Delete failed', err);
        alert('Failed to delete the task');
      }
    });
  }
}
