import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

interface NavItem {
  label: string;
  icon: string;
  route: string;
  roles?: string[];
}

interface NavGroup {
  title: string;
  items: NavItem[];
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule, MatIconModule],
  templateUrl: './sidebar.html',
  styleUrls: ['./sidebar.css'],
})
export class Sidebar {
  role = (localStorage.getItem('role') || '').toUpperCase();
  name = localStorage.getItem('name') || localStorage.getItem('email') || 'User';

  groups: NavGroup[] = [
    {
      title: 'Overview',
      items: [
        { label: 'Admin Dashboard', icon: 'space_dashboard', route: '/dashboard', roles: ['ADMIN'] },
        { label: 'Developer Board', icon: 'code', route: '/developer-board', roles: ['ADMIN', 'EMPLOYEE'] },
        { label: 'Tester Board', icon: 'bug_report', route: '/tester-board', roles: ['ADMIN', 'TESTER'] },
      ],
    },
    {
      title: 'Work',
      items: [
        { label: 'My Tasks', icon: 'task_alt', route: '/my-tasks' },
        { label: 'Task Board', icon: 'view_kanban', route: '/tasks' },
        { label: 'Create Task', icon: 'add_task', route: '/tasks/new', roles: ['ADMIN'] },
        { label: 'All Tasks', icon: 'list_alt', route: '/tasks/all', roles: ['ADMIN'] },
      ],
    },
    {
      title: 'Delivery',
      items: [
        { label: 'Project Overview', icon: 'insights', route: '/projects', roles: ['ADMIN'] },
        { label: 'My Projects', icon: 'folder_open', route: '/my-projects' },
        { label: 'Employee Directory', icon: 'groups', route: '/people', roles: ['ADMIN'] },
        { label: 'Registered Employees', icon: 'badge', route: '/people/accounts', roles: ['ADMIN'] },
      ],
    },
    {
      title: 'Time',
      items: [
        { label: 'Submit Timesheet', icon: 'edit_calendar', route: '/timesheets/new' },
        { label: 'View Timesheets', icon: 'table_view', route: '/timesheets', roles: ['ADMIN'] },
      ],
    },
    {
      title: 'Account',
      items: [
        { label: 'Update Profile', icon: 'manage_accounts', route: '/profile' },
        { label: 'Settings', icon: 'tune', route: '/settings' },
      ],
    },
  ];

  get initials(): string {
    return (this.name || 'U')
      .split(' ')
      .filter((part) => !!part)
      .slice(0, 2)
      .map((part) => part.charAt(0).toUpperCase())
      .join('');
  }

  isVisible(item: NavItem): boolean {
    if (!item.roles || item.roles.length === 0) {
      return true;
    }
    return this.role !== '' && item.roles.includes(this.role);
  }

  groupHasItems(group: NavGroup): boolean {
    return group.items.some((item) => this.isVisible(item));
  }
}
