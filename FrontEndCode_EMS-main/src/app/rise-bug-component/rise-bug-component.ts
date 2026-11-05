import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-rise-bug-component',
  imports:  [ BrowserModule, ReactiveFormsModule

],
  templateUrl: './rise-bug-component.html',
  styleUrls: ['./rise-bug-component.css']
})
export class RiseBugComponent {
   taskForm: FormGroup;

  projects = ['Project A', 'Project B', 'Project C']; // from API in real case
  modules = ['Module 1', 'Module 2']; // from API in real case

  constructor(private fb: FormBuilder) {
    this.taskForm = this.fb.group({
      title: ['', Validators.required],
      project: ['', Validators.required],
      module: ['', Validators.required],
      description: [''],
      steps: [''],
      attachment: [null],
      comments: ['']
    });
  }

  onFileChange(event: any) {
    const file = event.target.files[0];
    this.taskForm.patchValue({ attachment: file });
  }

  submitTask() {
    if (this.taskForm.valid) {
      console.log('Task Data:', this.taskForm.value);
      // call API here
    }
  }

}
