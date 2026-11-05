import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule, MatOptionModule } from '@angular/material/core';
import { MatDatepickerInput } from '@angular/material/datepicker';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { AllTasksDialogComponent } from '../all-tasks-dialog/all-tasks-dialog';
import { MatDialog } from '@angular/material/dialog';
import { RouterModule } from '@angular/router';


@Component({
  selector: 'app-create-task',
 imports: [
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,CommonModule,MatDatepickerModule,MatNativeDateModule,MatDatepickerInput,FormsModule ,MatOptionModule,MatSelectModule ,MatIconModule,
    RouterModule
  ],
  templateUrl: './create-task.html',
  styleUrl: './create-task.css'
})

export class CreateTask {
   taskForm: FormGroup;
  selectedFile: any;

modules: any[] = [];
subModules: string[] = [];
screens: string[] = [];
assignedUsers: any[] = [];
projects: any[] = [];

  constructor(private fb: FormBuilder,private http: HttpClient,private dialog: MatDialog) {
     this.taskForm = this.buildForm();
  }

  private buildForm(): FormGroup {
    return this.fb.group({
      project: ['', [Validators.required, Validators.maxLength(100)]],
      title: ['', [Validators.required, Validators.maxLength(100)]],
      assignedTo: ['', [Validators.required, Validators.maxLength(50)]],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      priority: ['', Validators.required],
      // Module, sub module and screen come from unmaintained master tables, so
      // they are free text rather than a blocking dropdown.
      module: ['', [Validators.maxLength(100)]],
      subModule: ['', [Validators.maxLength(100)]],
      screen: ['', [Validators.maxLength(100)]],
      description: ['', [Validators.maxLength(500)]]
    });
  }



onFileSelected(event: any): void {
  const file: File = event.target.files[0];
  const allowedTypes = ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 'image/png', 'image/jpeg'];

  if (file && allowedTypes.includes(file.type) && file.size <= 5 * 1024 * 1024) {
    this.selectedFile = file;
    console.log('Selected file: if', this.selectedFile);
  } else {
    alert('Invalid file type or size exceeds 5MB');
    this.selectedFile = null;
    console.log('Selected file: else', this.selectedFile);
  }
}
 onSubmit(): void {
  if (this.taskForm.invalid) return;

  const formData = new FormData();
  const formValue = this.taskForm.value;

  formData.append('project', formValue.project);
  formData.append('taskTitle', formValue.title);
  formData.append('description', formValue.description || '');
  formData.append('assignedTo', formValue.assignedTo);
  formData.append('priority', formValue.priority);
  formData.append('module', formValue.module || '');
  formData.append('subModule', formValue.subModule || '');
  formData.append('screen', formValue.screen || '');
  formData.append('startDate', this.toIsoDate(formValue.startDate));
  formData.append('endDate', this.toIsoDate(formValue.endDate));

  if (this.selectedFile && this.selectedFile instanceof File) {
    formData.append('attachment', this.selectedFile);
  }

   const token = localStorage.getItem('token');
  const headers = { Authorization: `Bearer ${token}` };

 this.http.post('http://localhost:9001/api/assigntasks/create', formData, { headers }).subscribe({
  next: (res: any) => {
    this.taskForm.reset();
    this.selectedFile = null;
    alert('Task created successfully!');
  },
  error: (err: any) => {
    console.error('Error submitting task:', err);
    alert('Task submission failed.');
  }
});
}

  private toIsoDate(value: any): string {
    const date = value instanceof Date ? value : new Date(value);
    return isNaN(date.getTime()) ? '' : date.toISOString().split('T')[0];
  }

ngOnInit(): void {
  this.taskForm = this.buildForm();

  // Default the schedule so the form only needs a title, owner and project.
  const start = new Date();
  const due = new Date();
  due.setDate(due.getDate() + 7);
  this.taskForm.patchValue({ startDate: start, endDate: due });

  this.fetchModules();
  this.fetchSubModules();
  this.fetchScreens();
  this.fetchAssignedUsers();
  this.fetchProjects();
}

fetchProjects(): void {
  const token = localStorage.getItem('token');
  const headers = { Authorization: `Bearer ${token}` };

  // The legacy `/api/assigntasks/getAllProjects` endpoint reads the empty master
  // project table, so the picker is fed from the projects actually in use.
  this.http.get<any[]>('http://localhost:9001/api/projects', { headers })
    .subscribe({
      next: (data) => {
        this.projects = (data || []).map((project) => ({
          id: project.id,
          projectName: project.name
        }));
      },
      error: (err) => {
        console.error('Error fetching projects:', err);
      }
    });
}
fetchAssignedUsers(): void {
  const token = localStorage.getItem('token');
  const headers = { Authorization: `Bearer ${token}` };

  this.http.get<any[]>('http://localhost:9001/api/assigntasks/getname', { headers })
    .subscribe({
      next: (data) => {
        this.assignedUsers = data;
        console.log('Users:', data);
      },
      error: (err) => {
        console.error('Error fetching assigned users:', err);
      }
    });
}
  fetchScreens(): void {
  const token = localStorage.getItem('token');
  const headers = { Authorization: `Bearer ${token}` };

  this.http.get<string[]>('http://localhost:9001/api/assigntasks/getcreateDesc', { headers })
    .subscribe({
      next: (data) => {
        this.screens = data;
      },
      error: (err) => {
        console.error('Error fetching screens:', err);
      }
    });
}
 fetchSubModules(): void {
  const token = localStorage.getItem('token');
  const headers = { Authorization: `Bearer ${token}` };

  this.http.get<string[]>('http://localhost:9001/api/assigntasks/getprocessDescription', { headers })
    .subscribe({
      next: (data) => {
        this.subModules = data;
      },
      error: (err) => {
        console.error('Error fetching sub modules:', err);
      }
    });
}

  fetchModules() {
       const token = localStorage.getItem('token');

     const headers = {
    Authorization: `Bearer ${token}` // Bearer scheme
  };
  this.http.get<any[]>('http://localhost:9001/api/assigntasks/getModuleDescription',{headers}).subscribe(data => {
    this.modules = data;
    //console.log('Modules fetched:', this.modules);
  });
}

  openTaskDialog(): void {
  const dialogRef = this.dialog.open(AllTasksDialogComponent, {
    width: '800px'
  });

  dialogRef.afterClosed().subscribe(task => {
    if (task) {
      this.taskForm.patchValue({
        title:task.taskTitle,
        module: task.module,
        subModule: task.subModule,
        screen: task.screen,
        description: task.description
      });

      // Optional: If attachment exists and should be auto-inserted
      if (task.attachmentPath) {
        // Just reference for now; actual upload logic may differ
        this.selectedFile = {
          name: task.attachmentPath.split('/').pop(),
          path: task.attachmentPath
        };
      }
    }
  });
}


}

