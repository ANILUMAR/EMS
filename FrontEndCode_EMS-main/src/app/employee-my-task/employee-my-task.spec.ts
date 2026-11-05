import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeMyTask } from './employee-my-task';

describe('EmployeeMyTask', () => {
  let component: EmployeeMyTask;
  let fixture: ComponentFixture<EmployeeMyTask>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeMyTask]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeeMyTask);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
