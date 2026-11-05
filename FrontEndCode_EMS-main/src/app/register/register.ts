import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';


@Component({
  standalone: true,
  selector: 'app-register',
  templateUrl: './register.html',
  styleUrls: ['./register.css'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,     // ✅ This is the important one!
    MatButtonModule,
    MatIconModule,
    MatCheckboxModule,
    RouterModule
  ]
})
export class Register {
  selectedFile: any;
  message: string | undefined;

  registerForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    name: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required, Validators.minLength(6)]),
    role: new FormControl('', [Validators.required]),
    address: new FormControl(''),
    phoneNumber: new FormControl('', [Validators.pattern(/^\d{10}$/)])
  });

  constructor(private http: HttpClient, private router: Router) {}

  onSubmit(): void {
    
    if (this.registerForm.invalid) {
      this.message = 'Please fill all required fields correctly.';
      return;
    }

    const formData = new FormData();

  // Append user data as JSON blob for @RequestPart("user")
  formData.append('user', new Blob([JSON.stringify(this.registerForm.value)], { type: 'application/json' }));

  // Append the file part
  if (this.selectedFile) {
    formData.append('file', this.selectedFile);
  }


    this.http.post('http://localhost:9001/api/auth/register', formData).subscribe({
      next: (res: any) => {
        this.message = 'Account created. Redirecting to sign in…';
        setTimeout(() => this.router.navigate(['/login']), 900);
      },
      error: (err: any) => {
        console.error('Error saving user:', err);
        this.message = 'Failed to register user. Please try again.';
      }
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
}