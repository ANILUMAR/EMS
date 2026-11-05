import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-update-profile',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    MatTooltipModule,
    RouterModule
  ],
  templateUrl: './update-profile.component.html',
  styleUrls: ['./update-profile.component.css']
})
export class UpdateProfileComponent implements OnInit {
  profileForm: FormGroup;
  selectedFile: File | null = null;
  profilePhotoUrl: string | ArrayBuffer | null = null;

  loading = true;
  saving = false;
  errorMessage = '';

  // Only an admin may reassign a role; the API ignores the field for everyone else.
  roles = ['ADMIN', 'EMPLOYEE', 'TESTER'];
  private storedImage = '';

  get canEditRole(): boolean {
    return (localStorage.getItem('role') || '').toUpperCase() === 'ADMIN';
  }

  constructor(private fb: FormBuilder, private snackBar: MatSnackBar, private http: HttpClient) {
    this.profileForm = this.fb.group({
      name: ['', Validators.required],
      email: [{ value: '', disabled: true }, [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.pattern(/^[0-9]{10}$/)]],
      address: [''],
      role: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadProfile();
  }

  /* ---------- display helpers ---------- */

  get displayName(): string {
    return this.profileForm.get('name')?.value || localStorage.getItem('name') || 'Your profile';
  }

  get email(): string {
    return this.profileForm.get('email')?.value || localStorage.getItem('email') || '';
  }

  get roleLabel(): string {
    const value = (this.profileForm.get('role')?.value || '').toLowerCase().replace(/_/g, ' ');
    return value ? value.charAt(0).toUpperCase() + value.slice(1) : 'No role';
  }

  get initials(): string {
    const parts = (this.displayName || 'U').split(/[\s.@]+/).filter((part) => !!part);
    return parts.slice(0, 2).map((part) => part.charAt(0).toUpperCase()).join('');
  }

  get completeness(): number {
    const checks = [this.profileForm.get('name')?.value, this.email, this.profileForm.get('phoneNumber')?.value, this.profileForm.get('address')?.value, this.storedImage || this.selectedFile];
    const filled = checks.filter((value) => !!value).length;
    return Math.round((filled / checks.length) * 100);
  }

  /* ---------- data ---------- */

  loadProfile(): void {
    const token = localStorage.getItem('token');
    const username = localStorage.getItem('email');

    if (!token || !username) {
      this.errorMessage = 'You need to sign in again to edit your profile.';
      this.loading = false;
      return;
    }

    this.loading = true;
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const url = `http://localhost:9001/api/UserProfile/userdetails?username=${encodeURIComponent(username)}`;

    this.http.get<any>(url, { headers }).subscribe({
      next: (data: any) => {
        this.profileForm.patchValue({
          name: data.name || data.username || data.fullName || '',
          email: data.email || username,
          phoneNumber: data.phoneNumber || '',
          address: data.address || '',
          role: (data.role || '').toUpperCase()
        });

        this.storedImage = data.registerImage || '';
        if (this.storedImage) {
          this.profilePhotoUrl = this.storedImage.startsWith('http')
            ? this.storedImage
            : `http://localhost:9001/attachments/${this.storedImage}`;
        }

        this.loading = false;
      },
      error: (err: any) => {
        console.error('Failed to load profile', err);
        this.errorMessage = 'Could not load your profile details right now.';
        this.loading = false;
      }
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.selectedFile = input.files && input.files.length ? input.files[0] : null;

    if (this.selectedFile) {
      const reader = new FileReader();
      reader.onload = (readerEvent) => {
        this.profilePhotoUrl = readerEvent.target?.result ?? null;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  onSubmit(): void {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      this.snackBar.open('Please check the highlighted fields.', 'Close', { duration: 3000 });
      return;
    }

    const value = this.profileForm.getRawValue();

    const details = {
      username: value.email,
      email: value.email,
      fullName: value.name,
      role: value.role,
      address: value.address || null,
      phoneNumber: value.phoneNumber || null
    };

    const formData = new FormData();
    formData.append('details', new Blob([JSON.stringify(details)], { type: 'application/json' }));
    if (this.selectedFile) {
      formData.append('profilePhoto', this.selectedFile);
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${localStorage.getItem('token') || ''}`);

    this.saving = true;
    this.http.put('http://localhost:9001/api/UserProfile/update', formData, { headers }).subscribe({
      next: () => {
        this.saving = false;
        localStorage.setItem('name', value.name);
        localStorage.setItem('role', value.role);
        this.selectedFile = null;
        this.snackBar.open('Profile updated.', 'Close', { duration: 3000 });
        this.loadProfile();
      },
      error: (err) => {
        console.error('Profile update failed', err);
        this.saving = false;
        this.snackBar.open('Profile update failed. Please try again.', 'Close', { duration: 4000 });
      }
    });
  }

  reset(): void {
    this.selectedFile = null;
    this.profilePhotoUrl = this.storedImage ? `http://localhost:9001/attachments/${this.storedImage}` : null;
    this.loadProfile();
  }
}
