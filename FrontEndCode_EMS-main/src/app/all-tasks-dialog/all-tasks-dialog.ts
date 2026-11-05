import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { HttpHeaders } from '@angular/common/http';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-all-tasks-dialog',
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule
  ],
  templateUrl: './all-tasks-dialog.html',
  styleUrl: './all-tasks-dialog.css'
})
export class AllTasksDialogComponent {
  tasks: any[] = [];
  search = '';

  private apiUrl = 'http://localhost:9001/api/assigntasks/getAssignTask';

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    public dialogRef: MatDialogRef<AllTasksDialogComponent>
  ) {}

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    const email = localStorage.getItem('email');

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.http.get<any[]>(`${this.apiUrl}?email=${email}`, { headers }).subscribe({
      next: (data) => {
        this.tasks = data || [];
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error fetching tasks', err)
    });
  }

  get filteredTasks(): any[] {
    const term = this.search.trim().toLowerCase();
    if (!term) return this.tasks;
    return this.tasks.filter((task) =>
      [task.taskTitle, task.module, task.subModule, task.screen]
        .filter(Boolean)
        .some((value) => String(value).toLowerCase().includes(term))
    );
  }

  onTaskDoubleClick(task: any): void {
    this.dialogRef.close(task);
  }
}
