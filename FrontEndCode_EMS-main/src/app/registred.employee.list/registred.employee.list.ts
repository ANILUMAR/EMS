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
import { WorkspaceService } from '../services/workspace.service';

interface RegisteredEmployee {
  id: number;
  name: string;
  email: string;
  role: string;
  phoneNumber?: string;
  address?: string;
  registerImage?: string;
  generatedDate?: string;
}

@Component({
  selector: 'app-registred.employee.list',
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
  templateUrl: './registred.employee.list.html',
  styleUrl: './registred.employee.list.css'
})
export class RegistredEmployeeList implements OnInit {
  employees: RegisteredEmployee[] = [];
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

  get filteredEmployees(): RegisteredEmployee[] {
    const term = this.search.trim().toLowerCase();
    return this.employees.filter((employee) => {
      const matchesRole = this.role === 'ALL' || (employee.role || '').toUpperCase() === this.role;
      const matchesSearch =
        !term ||
        [employee.name, employee.email, employee.role, employee.phoneNumber]
          .filter(Boolean)
          .some((value) => String(value).toLowerCase().includes(term));
      return matchesRole && matchesSearch;
    });
  }

  get adminCount(): number {
    return this.employees.filter((employee) => (employee.role || '').toUpperCase() === 'ADMIN').length;
  }

  get employeeCount(): number {
    return this.employees.length - this.adminCount;
  }

  get newThisMonth(): number {
    const start = new Date();
    start.setDate(1);
    start.setHours(0, 0, 0, 0);
    return this.employees.filter((employee) => {
      const joined = this.toDate(employee.generatedDate);
      return !!joined && joined.getTime() >= start.getTime();
    }).length;
  }

  /* ---------- data ---------- */

  reload(): void {
    this.loading = true;
    this.errorMessage = '';

    this.workspace.getEmployees().subscribe({
      next: (data: RegisteredEmployee[]) => {
        this.employees = data || [];
        this.loading = false;
      },
      error: (err: any) => {
        console.error('Failed to load registered employees', err);
        this.errorMessage = 'Could not load registered employees right now.';
        this.loading = false;
      }
    });
  }

  editEmployee(employee: RegisteredEmployee): void {
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

  private toDate(value?: string): Date | null {
    if (!value) {
      return null;
    }
    const parsed = new Date(value);
    return isNaN(parsed.getTime()) ? null : parsed;
  }

  registeredOn(employee: RegisteredEmployee): string {
    const date = this.toDate(employee.generatedDate);
    return date ? date.toLocaleDateString(undefined, { day: '2-digit', month: 'short', year: 'numeric' }) : '—';
  }

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

  hasPhoto(employee: RegisteredEmployee): boolean {
    return !!employee.registerImage && !this.failedImages.has(employee.email);
  }

  photoUrl(employee: RegisteredEmployee): string {
    return 'http://localhost:9001/attachments/' + employee.registerImage;
  }

  onImageError(employee: RegisteredEmployee): void {
    this.failedImages.add(employee.email);
  }
}
