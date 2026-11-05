import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterModule } from '@angular/router';
import { EditEmployeeComponent } from '../edit-employee.ts/edit-employee.ts';
import { Employee } from '../models/employee';
import { WorkspaceService } from '../services/workspace.service';

@Component({
  selector: 'app-employee-directory',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatTooltipModule,
    RouterModule
  ],
  templateUrl: './employee-directory.component.html',
  styleUrls: ['./employee-directory.component.css']
})
export class EmployeeDirectoryComponent implements OnInit {
  employees: Employee[] = [];
  loading = true;
  errorMessage = '';

  search = '';
  role = 'ALL';

  private failedImages = new Set<string>();

  constructor(private workspace: WorkspaceService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.reload();
  }

  /* ---------- derived data ---------- */

  get filteredEmployees(): Employee[] {
    const term = this.search.trim().toLowerCase();
    return this.employees.filter((employee) => {
      const matchesRole = this.role === 'ALL' || (employee.role || '').toUpperCase() === this.role;
      const matchesSearch =
        !term ||
        [employee.name, employee.email, employee.role, employee.designation]
          .filter(Boolean)
          .some((value) => String(value).toLowerCase().includes(term));
      return matchesRole && matchesSearch;
    });
  }

  get roleChips(): { key: string; label: string; count: number }[] {
    const counts = new Map<string, number>();
    this.employees.forEach((employee) => {
      const key = (employee.role || 'UNASSIGNED').toUpperCase();
      counts.set(key, (counts.get(key) || 0) + 1);
    });

    const chips = Array.from(counts.entries())
      .sort((a, b) => b[1] - a[1])
      .map(([key, count]) => ({ key, label: this.roleLabel(key), count }));

    return [{ key: 'ALL', label: 'Everyone', count: this.employees.length }, ...chips];
  }

  get rolesCovered(): number {
    return new Set(this.employees.map((employee) => (employee.role || '').toUpperCase()).filter(Boolean)).size;
  }

  get adminCount(): number {
    return this.employees.filter((employee) => (employee.role || '').toUpperCase() === 'ADMIN').length;
  }

  get photoCount(): number {
    return this.employees.filter((employee) => !!employee.registerImage).length;
  }

  /* ---------- data ---------- */

  reload(): void {
    this.loading = true;
    this.errorMessage = '';

    this.workspace.getEmployees().subscribe({
      next: (data: Employee[]) => {
        this.employees = data || [];
        this.loading = false;
      },
      error: (err: any) => {
        console.error('Failed to load employees', err);
        this.errorMessage = 'Could not load the employee directory right now.';
        this.loading = false;
      }
    });
  }

  filterByRole(key: string): void {
    this.role = key;
  }

  editEmployee(employee: Employee): void {
    this.dialog
      .open(EditEmployeeComponent, { width: '440px', data: { ...employee } })
      .afterClosed()
      .subscribe((updated) => {
        if (updated) {
          this.reload();
        }
      });
  }

  /* ---------- presentation helpers ---------- */

  roleLabel(role: string): string {
    const value = (role || '').toLowerCase().replace(/_/g, ' ');
    return value ? value.charAt(0).toUpperCase() + value.slice(1) : 'Unassigned';
  }

  roleBadge(role: string): string {
    return (role || '').toUpperCase() === 'ADMIN' ? 'ems-badge--info' : 'ems-badge--neutral';
  }

  initials(name: string): string {
    const parts = (name || '?').split(' ').filter((part) => !!part);
    return parts.slice(0, 2).map((part) => part.charAt(0).toUpperCase()).join('');
  }

  hasPhoto(employee: Employee): boolean {
    return !!employee.registerImage && !this.failedImages.has(employee.email);
  }

  photoUrl(employee: Employee): string {
    return 'http://localhost:9001/attachments/' + employee.registerImage;
  }

  onImageError(employee: Employee): void {
    this.failedImages.add(employee.email);
  }
}
