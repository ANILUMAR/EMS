import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewTimesheets } from './view-timesheets';

describe('ViewTimesheets', () => {
  let component: ViewTimesheets;
  let fixture: ComponentFixture<ViewTimesheets>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewTimesheets]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewTimesheets);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
