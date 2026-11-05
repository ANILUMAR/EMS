import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AllTaskComponent } from './all-task-component/all-task-component';
import { AuthGuard } from './auth-guard';
import { CreateTask } from './create-task/create-task';
import { Dashboard } from './dashboard/dashboard';
import { EmployeeDashboardComponent } from './employee-dashboard.component/employee-dashboard.component';
import { EmployeeDirectoryComponent } from './employee-directory.component/employee-directory.component';
import { EmployeeMyTask } from './employee-my-task/employee-my-task';
import { Forgotpassward } from './forgotpassward/forgotpassward';
import { Loading } from './loading/loading';
import { Login } from './login/login';
import { Menu } from './menu/menu';
import { MyProjectsComponent } from './my-projects.component.ts/my-projects.component.ts';
import { ProjectOverviewComponent } from './project-overview.component/project-overview.component';
import { Register } from './register/register';
import { RegistredEmployeeList } from './registred.employee.list/registred.employee.list';
import { RoleGuard } from './role-guard';
import { SubmitTimesheetComponent } from './submit-timesheet.component.ts/submit-timesheet.component.ts';
import { TaskListComponent } from './task-list.component/task-list.component';
import { TesterDashboardComponent } from './tester-dashboard.component/tester-dashboard.component';
import { UpdateProfileComponent } from './update-profile.component/update-profile.component';
import { ViewTimesheets } from './view-timesheets/view-timesheets';

export const routes: Routes = [
  { path: '', component: Loading },

  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'forgotpassward', component: Forgotpassward },

  // The three role surfaces. `data.roles` is enforced by RoleGuard: the admin
  // sees everything, the developer and the tester only ever reach their own work.
  { path: 'dashboard', component: Dashboard, canActivate: [AuthGuard, RoleGuard], data: { roles: ['ADMIN'] } },
  { path: 'developer-board', component: EmployeeDashboardComponent, canActivate: [AuthGuard, RoleGuard], data: { roles: ['ADMIN', 'EMPLOYEE'] } },
  { path: 'tester-board', component: TesterDashboardComponent, canActivate: [AuthGuard, RoleGuard], data: { roles: ['ADMIN', 'TESTER'] } },

  { path: 'my-tasks', component: EmployeeMyTask, canActivate: [AuthGuard, RoleGuard] },
  { path: 'tasks', component: TaskListComponent, canActivate: [AuthGuard, RoleGuard] },
  { path: 'tasks/new', component: CreateTask, canActivate: [AuthGuard, RoleGuard], data: { roles: ['ADMIN'] } },
  { path: 'tasks/all', component: AllTaskComponent, canActivate: [AuthGuard, RoleGuard], data: { roles: ['ADMIN'] } },

  { path: 'projects', component: ProjectOverviewComponent, canActivate: [AuthGuard, RoleGuard], data: { roles: ['ADMIN'] } },
  { path: 'my-projects', component: MyProjectsComponent, canActivate: [AuthGuard, RoleGuard] },
  { path: 'people', component: EmployeeDirectoryComponent, canActivate: [AuthGuard, RoleGuard], data: { roles: ['ADMIN'] } },
  { path: 'people/accounts', component: RegistredEmployeeList, canActivate: [AuthGuard, RoleGuard], data: { roles: ['ADMIN'] } },

  { path: 'timesheets', component: ViewTimesheets, canActivate: [AuthGuard, RoleGuard], data: { roles: ['ADMIN'] } },
  { path: 'timesheets/new', component: SubmitTimesheetComponent, canActivate: [AuthGuard, RoleGuard] },

  { path: 'profile', component: UpdateProfileComponent, canActivate: [AuthGuard] },
  { path: 'settings', component: Menu, canActivate: [AuthGuard] },

  // Legacy dotted paths kept so older bookmarks still resolve in-app.
  { path: 'employee-dashboard.component', redirectTo: 'developer-board', pathMatch: 'full' },
  { path: 'tester-dashboard.component', redirectTo: 'tester-board', pathMatch: 'full' },
  { path: 'employee-my-task', redirectTo: 'my-tasks', pathMatch: 'full' },
  { path: 'task-list.component', redirectTo: 'tasks', pathMatch: 'full' },
  { path: 'create-task', redirectTo: 'tasks/new', pathMatch: 'full' },
  { path: 'all-task-component', redirectTo: 'tasks/all', pathMatch: 'full' },
  { path: 'project-overview.component', redirectTo: 'projects', pathMatch: 'full' },
  { path: 'my-projects.component', redirectTo: 'my-projects', pathMatch: 'full' },
  { path: 'employee-directory.component', redirectTo: 'people', pathMatch: 'full' },
  { path: 'registred.employee.list', redirectTo: 'people/accounts', pathMatch: 'full' },
  { path: 'submit-timesheet.component', redirectTo: 'timesheets/new', pathMatch: 'full' },
  { path: 'view-timesheets', redirectTo: 'timesheets', pathMatch: 'full' },
  { path: 'update-profile.component', redirectTo: 'profile', pathMatch: 'full' },
  { path: 'menu', redirectTo: 'settings', pathMatch: 'full' },

  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
