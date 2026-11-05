import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistredEmployeeList } from './registred.employee.list';

describe('RegistredEmployeeList', () => {
  let component: RegistredEmployeeList;
  let fixture: ComponentFixture<RegistredEmployeeList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistredEmployeeList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistredEmployeeList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
