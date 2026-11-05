import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterModule } from '@angular/router';
import { TimesheetEntry } from '../models/employee';
import { Timesheet } from '../services/timesheet';

@Component({
  selector: 'app-view-timesheets',
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
  templateUrl: './view-timesheets.html',
  styleUrl: './view-timesheets.css'
})
export class ViewTimesheets implements OnInit {
  entries: TimesheetEntry[] = [];
  loading = true;
  errorMessage = '';

  search = '';
  status = 'ALL';
  mineOnly = false;

  constructor(private timesheetService: Timesheet) {}

  ngOnInit(): void {
    this.loadTimesheets();
  }

  /* ---------- derived data ---------- */

  get filtered(): TimesheetEntry[] {
    const term = this.search.trim().toLowerCase();
    const email = (localStorage.getItem('email') || '').toLowerCase();
    const name = (localStorage.getItem('name') || '').toLowerCase();

    return this.entries
      .filter((entry) => {
        const owner = (entry.employeeName || '').toLowerCase();
        const matchesMine = !this.mineOnly || owner === email || (!!name && owner === name);
        const matchesStatus = this.status === 'ALL' || (entry.status || '').toUpperCase() === this.status;
        const matchesSearch =
          !term ||
          [entry.employeeName, entry.project, entry.date, entry.status]
            .filter(Boolean)
            .some((value) => String(value).toLowerCase().includes(term));
        return matchesMine && matchesStatus && matchesSearch;
      })
      .sort((a, b) => (a.date < b.date ? 1 : -1));
  }

  get totalHours(): number {
    return this.filtered.reduce((sum, entry) => sum + Number(entry.hours || 0), 0);
  }

  get pendingCount(): number {
    return this.entries.filter((entry) => (entry.status || '').toUpperCase() === 'PENDING').length;
  }

  get approvedCount(): number {
    return this.entries.filter((entry) => (entry.status || '').toUpperCase() === 'APPROVED').length;
  }

  get statusChips(): { key: string; label: string; count: number }[] {
    const keys = ['PENDING', 'APPROVED', 'REJECTED', 'DRAFT'];
    return [
      { key: 'ALL', label: 'All', count: this.entries.length },
      ...keys.map((key) => ({
        key,
        label: key.charAt(0) + key.slice(1).toLowerCase(),
        count: this.entries.filter((entry) => (entry.status || '').toUpperCase() === key).length
      }))
    ];
  }

  /* ---------- data ---------- */

  loadTimesheets(): void {
    this.loading = true;
    this.errorMessage = '';

    this.timesheetService.getTimesheets().subscribe({
      next: (data: TimesheetEntry[]) => {
        this.entries = data || [];
        this.loading = false;
      },
      error: (err: any) => {
        console.error('Failed to load timesheets', err);
        this.errorMessage = 'Could not load timesheets right now.';
        this.loading = false;
      }
    });
  }

  /* ---------- presentation helpers ---------- */

  initials(name: string): string {
    const parts = (name || '?').split(/[\s.@]+/).filter((part) => !!part);
    return parts.slice(0, 2).map((part) => part.charAt(0).toUpperCase()).join('');
  }

  statusBadge(status: string): string {
    switch ((status || '').toUpperCase()) {
      case 'APPROVED': return 'ems-badge--success';
      case 'REJECTED': return 'ems-badge--danger';
      case 'PENDING': return 'ems-badge--warning';
      default: return 'ems-badge--neutral';
    }
  }

  statusLabel(status: string): string {
    const value = (status || 'Unknown').toLowerCase();
    return value.charAt(0).toUpperCase() + value.slice(1);
  }

  formatDate(date: string): string {
    if (!date) {
      return '—';
    }
    const parsed = new Date(date);
    return isNaN(parsed.getTime())
      ? date
      : parsed.toLocaleDateString(undefined, { day: '2-digit', month: 'short', year: 'numeric' });
  }

  weekDay(date: string): string {
    const parsed = new Date(date);
    return isNaN(parsed.getTime()) ? '' : parsed.toLocaleDateString(undefined, { weekday: 'short' });
  }
}
