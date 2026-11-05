import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterModule } from '@angular/router';
import { forkJoin } from 'rxjs';
import { ProjectItem, TaskItem, WorkspaceService } from '../services/workspace.service';

interface ProjectRow {
  id: number;
  name: string;
  description: string;
  owner: string;
  total: number;
  done: number;
  progress: number;
  contributors: string[];
  status: string;
  statusClass: string;
}

@Component({
  selector: 'app-project-overview',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatIconModule, MatTooltipModule, RouterModule],
  templateUrl: './project-overview.component.html',
  styleUrls: ['./project-overview.component.css']
})
export class ProjectOverviewComponent implements OnInit {
  loading = true;
  errorMessage = '';

  rows: ProjectRow[] = [];
  selectedId: number | null = null;
  private allTasks: TaskItem[] = [];

  get selectedTasks(): TaskItem[] {
    const project = this.selected;
    return project ? this.allTasks.filter((task) => this.taskBelongsTo(task, { id: project.id, name: project.name } as ProjectItem)) : [];
  }

  constructor(private workspace: WorkspaceService) {}

  ngOnInit(): void {
    this.reload();
  }

  /* ---------- portfolio level numbers ---------- */

  get totalProjects(): number {
    return this.rows.length;
  }

  get totalTasks(): number {
    return this.rows.reduce((sum, row) => sum + row.total, 0);
  }

  get openTasks(): number {
    return this.rows.reduce((sum, row) => sum + (row.total - row.done), 0);
  }

  get completionRate(): number {
    return this.totalTasks ? Math.round((this.rows.reduce((sum, row) => sum + row.done, 0) / this.totalTasks) * 100) : 0;
  }

  get contributorCount(): number {
    const people = new Set<string>();
    this.rows.forEach((row) => row.contributors.forEach((person) => people.add(person)));
    return people.size;
  }

  get selected(): ProjectRow | null {
    return this.rows.find((row) => row.id === this.selectedId) || this.rows[0] || null;
  }

  /* ---------- loading ---------- */

  reload(): void {
    this.loading = true;
    this.errorMessage = '';

    forkJoin({
      projects: this.workspace.getProjects(),
      tasks: this.workspace.getTasks()
    }).subscribe({
      next: ({ projects, tasks }) => {
        this.allTasks = tasks || [];
        this.rows = (projects || []).map((project) => this.toRow(project, this.allTasks));
        if (!this.rows.some((row) => row.id === this.selectedId)) {
          this.selectedId = this.rows[0]?.id ?? null;
        }
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load portfolio', err);
        this.errorMessage = 'Could not load the project portfolio right now.';
        this.loading = false;
      }
    });
  }

  select(row: ProjectRow): void {
    this.selectedId = row.id;
  }

  /* ---------- helpers ---------- */

  private toRow(project: ProjectItem, tasks: TaskItem[]): ProjectRow {
    const projectTasks = tasks.filter((task) => this.taskBelongsTo(task, project));
    const done = projectTasks.filter((task) => task.status === 'DONE').length;
    const progress = projectTasks.length ? Math.round((done / projectTasks.length) * 100) : 0;

    return {
      id: project.id,
      name: project.name || 'Untitled project',
      description: project.description || 'No description captured for this project yet.',
      owner: this.displayName(project.user) || 'Unassigned',
      total: projectTasks.length,
      done,
      progress,
      contributors: this.uniqueNames(projectTasks),
      ...this.statusFor(projectTasks.length, done, progress)
    };
  }

  private taskBelongsTo(task: TaskItem, project: ProjectItem): boolean {
    if (task.project && typeof task.project === 'object') {
      return task.project.id === project.id;
    }
    const label = String(task.project || '').toLowerCase();
    return !!label && label === String(project.name || '').toLowerCase();
  }

  private statusFor(total: number, done: number, progress: number): { status: string; statusClass: string } {
    if (!total) {
      return { status: 'Not started', statusClass: 'ems-badge--neutral' };
    }
    if (done === total) {
      return { status: 'Completed', statusClass: 'ems-badge--success' };
    }
    if (progress >= 50) {
      return { status: 'On track', statusClass: 'ems-badge--info' };
    }
    return { status: 'At risk', statusClass: 'ems-badge--warning' };
  }

  private uniqueNames(tasks: TaskItem[]): string[] {
    const names = tasks.map((task) => this.displayName(task.user)).filter((name): name is string => !!name);
    return Array.from(new Set(names));
  }

  private displayName(user: any): string {
    if (!user) {
      return '';
    }
    return user.username || user.name || user.email || '';
  }

  initials(name: string): string {
    const parts = (name || '?').split(' ').filter((part) => !!part);
    return parts.slice(0, 2).map((part) => part.charAt(0).toUpperCase()).join('');
  }

  statusBadge(task: TaskItem): string {
    switch ((task.status || '').toUpperCase()) {
      case 'DONE': return 'ems-badge--success';
      case 'IN_PROGRESS': return 'ems-badge--info';
      default: return 'ems-badge--neutral';
    }
  }

  statusLabel(task: TaskItem): string {
    return (task.status || 'TODO').replace('_', ' ').toLowerCase();
  }

  fillClass(progress: number): string {
    if (progress >= 100) {
      return 'is-success';
    }
    return progress > 0 ? 'is-info' : 'is-neutral';
  }
}
