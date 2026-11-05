import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogContent, MatDialogActions } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth-service';

@Component({
  selector: 'app-edit-employee',
  standalone: true,   // ✅ mark as standalone
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDialogContent,
    MatDialogActions
],
  template: `
    <h2 mat-dialog-title>Edit Employee</h2>

    <mat-dialog-content>
      <mat-form-field appearance="outline" class="w-full">
        <mat-label>Name</mat-label>
        <input matInput [(ngModel)]="data.name">
      </mat-form-field>

      <mat-form-field appearance="outline" class="w-full">
        <mat-label>Email</mat-label>
        <input matInput [(ngModel)]="data.email" readonly>
      </mat-form-field>

       <mat-form-field appearance="outline" class="w-full">
        <mat-label>Role</mat-label>
        <input matInput [(ngModel)]="data.role">
      </mat-form-field>

      

      <mat-form-field appearance="outline" class="w-full">
        <mat-label>Phone</mat-label>
        <input matInput [(ngModel)]="data.phoneNumber">
      </mat-form-field>

      <mat-form-field appearance="outline" class="w-full">
        <mat-label>Address</mat-label>
        <input matInput [(ngModel)]="data.address">
      </mat-form-field>

      
      <div class="w-full">
        <label class="mat-label">Upload Image</label>
        <input type="file" (change)="onFileSelected($event)">
      </div>

    </mat-dialog-content>

    <mat-dialog-actions align="end">
      <button mat-button (click)="onClose()">Cancel</button>
      <button mat-flat-button color="primary" (click)="update()">update</button>
    </mat-dialog-actions>
  `
})
export class EditEmployeeComponent {
   selectedFile?: File;
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<EditEmployeeComponent>,
    private authservice:AuthService
  ) {}

  onClose() {
    this.dialogRef.close();
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  update() {
    console.log("Updated Employee:", this.data);

    this.authservice.updateUser(this.data.id, this.data, this.selectedFile).subscribe({
      next: (response) => {
        console.log('User updated successfully', response);
        this.dialogRef.close(response); // return updated user or success message
      },
      error: (err) => {
        console.error('Update failed', err);
        // Optionally show error to user
      }
    });
  }
}
