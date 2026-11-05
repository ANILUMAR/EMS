import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyProjectsComponentTs } from './my-projects.component.ts';

describe('MyProjectsComponentTs', () => {
  let component: MyProjectsComponentTs;
  let fixture: ComponentFixture<MyProjectsComponentTs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyProjectsComponentTs]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyProjectsComponentTs);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
