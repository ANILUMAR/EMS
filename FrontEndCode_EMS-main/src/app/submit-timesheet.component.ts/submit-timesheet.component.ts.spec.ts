import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmitTimesheetComponentTs } from './submit-timesheet.component.ts';

describe('SubmitTimesheetComponentTs', () => {
  let component: SubmitTimesheetComponentTs;
  let fixture: ComponentFixture<SubmitTimesheetComponentTs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubmitTimesheetComponentTs]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubmitTimesheetComponentTs);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
