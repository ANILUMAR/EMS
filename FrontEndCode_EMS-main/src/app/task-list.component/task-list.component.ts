import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterModule } from '@angular/router';
import { TaskModel } from '../models/employee';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatTooltipModule,
    RouterModule
  ],
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements OnInit {
  tasks: TaskModel[] = [];
  loading = true;
  errorMessage = '';

  search = '';
  priority = 'ALL';

  private apiUrl = 'http://localhost:9001/api/assigntasks/getAssignTask';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.taskModelResponse();
  }

  get isAdmin(): boolean {
    return (localStorage.getItem('role') || '').toUpperCase() === 'ADMIN';
  }

  get filteredTasks(): TaskModel[] {
    const term = this.search.trim().toLowerCase();
    return this.tasks.filter((task) => {
      const matchesPriority = this.priority === 'ALL' || (task.priority || '').toUpperCase() === this.priority;
      const matchesSearch =
        !term ||
        [task.taskTitle, task.project, task.assignedToName, task.module, task.screen]
          .filter(Boolean)
          .some((value) => String(value).toLowerCase().includes(term));
      return matchesPriority && matchesSearch;
    });
  }

  get priorityCounts(): { key: string; label: string; count: number }[] {
    return [
      { key: 'ALL', label: 'All', count: this.tasks.length },
      { key: 'HIGH', label: 'High', count: this.countByPriority('HIGH') },
      { key: 'MEDIUM', label: 'Medium', count: this.countByPriority('MEDIUM') },
      { key: 'LOW', label: 'Low', count: this.countByPriority('LOW') }
    ];
  }

  get overdueCount(): number {
    const today = new Date().setHours(0, 0, 0, 0);
    return this.tasks.filter((task) => task.endDate && new Date(task.endDate).getTime() < today).length;
  }

  countByPriority(priority: string): number {
    return this.tasks.filter((task) => (task.priority || '').toUpperCase() === priority).length;
  }

  setPriority(priority: string): void {
    this.priority = priority;
  }

  taskModelResponse(): void {
    this.loading = true;
    const token = localStorage.getItem('token');
    const email = localStorage.getItem('email');

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.http.get<TaskModel[]>(`${this.apiUrl}?email=${email}`, { headers }).subscribe({
      next: (tasks: TaskModel[]) => {
        this.tasks = tasks || [];
        this.loading = false;
      },
      error: (err: any) => {
        console.error('Failed to fetch tasks', err);
        this.errorMessage = 'Could not load tasks right now.';
        this.loading = false;
      }
    });
  }

  priorityClass(priority: string): string {
    switch ((priority || '').toUpperCase()) {
      case 'HIGH': return 'is-high';
      case 'MEDIUM': return 'is-medium';
      case 'LOW': return 'is-low';
      default: return '';
    }
  }

  priorityBadge(priority: string): string {
    switch ((priority || '').toUpperCase()) {
      case 'HIGH': return 'ems-badge--danger';
      case 'MEDIUM': return 'ems-badge--warning';
      case 'LOW': return 'ems-badge--success';
      default: return 'ems-badge--neutral';
    }
  }

  initials(name: string): string {
    return (name || '?')
      .split(' ')
      .filter((part) => !!part)
      .slice(0, 2)
      .map((part) => part.charAt(0).toUpperCase())
      .join('');
  }

  isImage(filename: string): boolean {
    const ext = (filename || '').toLowerCase();
    return ext.endsWith('.png') || ext.endsWith('.jpg') || ext.endsWith('.jpeg') || ext.endsWith('.gif');
  }

  attachmentUrl(filename: string): string {
    return 'http://localhost:9001/attachments/' + filename;
  }

  openFilePopup(filename: string): void {
    window.open(this.attachmentUrl(filename), '_blank', 'width=800,height=600,resizable=yes,scrollbars=yes');
  }

  deleteAttachment(id: number): void {
    const token = localStorage.getItem('token');
    const deletedBy = localStorage.getItem('email');

    const reason = prompt('Please enter a reason for deleting this task:');
    if (!reason) {
      return;
    }

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    const body = { taskId: id, reason, deletedBy };

    this.http.post('http://localhost:9001/api/assigntasks/delete', body, { headers, responseType: 'text' }).subscribe({
      next: (res) => {
        alert(res);
        this.taskModelResponse();
      },
      error: (err) => {
        console.error('Delete failed:', err);
        alert('Failed to delete the task');
      }
    });
  }
}
