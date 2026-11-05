import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllTasksDialog } from './all-tasks-dialog';

describe('AllTasksDialog', () => {
  let component: AllTasksDialog;
  let fixture: ComponentFixture<AllTasksDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AllTasksDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AllTasksDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
