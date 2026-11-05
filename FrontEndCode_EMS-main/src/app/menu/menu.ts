import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router, RouterModule } from '@angular/router';

type PrefKey = 'dailyDigest' | 'desktopAlerts' | 'weeklySummary' | 'autoDraft';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatIconModule, MatTooltipModule, RouterModule],
  templateUrl: './menu.html',
  styleUrl: './menu.css'
})
export class Menu {
  name = localStorage.getItem('name') || localStorage.getItem('email') || 'User';
  email = localStorage.getItem('email') || '';
  role = (localStorage.getItem('role') || 'EMPLOYEE').toUpperCase();

  defaultHours = Number(localStorage.getItem('pref.defaultHours') || 8);
  weekStart = localStorage.getItem('pref.weekStart') || 'Monday';
  hourOptions = [6, 7, 8, 9];

  prefs: Record<PrefKey, boolean> = {
    dailyDigest: this.read('pref.dailyDigest', true),
    desktopAlerts: this.read('pref.desktopAlerts', true),
    weeklySummary: this.read('pref.weeklySummary', false),
    autoDraft: this.read('pref.autoDraft', false)
  };

  preferenceRows: { key: PrefKey; title: string; hint: string; icon: string }[] = [
    { key: 'dailyDigest', title: 'Daily digest', hint: 'A morning summary of your assigned tasks.', icon: 'inbox' },
    { key: 'desktopAlerts', title: 'Due date alerts', hint: 'Warn me when a task is one day from its due date.', icon: 'notifications_active' },
    { key: 'weeklySummary', title: 'Weekly summary', hint: 'Email a recap of hours logged every Friday.', icon: 'summarize' },
    { key: 'autoDraft', title: 'Keep timesheet drafts', hint: 'Save an unfinished timesheet instead of discarding it.', icon: 'save' }
  ];

  constructor(private snackBar: MatSnackBar, private router: Router) {}

  /* ---------- preferences ---------- */

  private read(key: string, fallback: boolean): boolean {
    const stored = localStorage.getItem(key);
    return stored === null ? fallback : stored === 'true';
  }

  toggle(key: PrefKey): void {
    this.prefs[key] = !this.prefs[key];
    localStorage.setItem(`pref.${key}`, String(this.prefs[key]));
    this.confirm();
  }

  setHours(hours: number): void {
    this.defaultHours = hours;
    localStorage.setItem('pref.defaultHours', String(hours));
    this.confirm();
  }

  setWeekStart(day: string): void {
    this.weekStart = day;
    localStorage.setItem('pref.weekStart', day);
    this.confirm();
  }

  private confirm(): void {
    this.snackBar.open('Preferences saved.', 'Close', { duration: 2000 });
  }

  /* ---------- session ---------- */

  get initials(): string {
    const parts = (this.name || 'U').split(/[\s.@]+/).filter((part) => !!part);
    return parts.slice(0, 2).map((part) => part.charAt(0).toUpperCase()).join('');
  }

  get roleLabel(): string {
    const value = this.role.toLowerCase().replace(/_/g, ' ');
    return value.charAt(0).toUpperCase() + value.slice(1);
  }

  signOut(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
