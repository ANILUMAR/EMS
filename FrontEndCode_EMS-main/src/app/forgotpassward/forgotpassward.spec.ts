import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Forgotpassward } from './forgotpassward';

describe('Forgotpassward', () => {
  let component: Forgotpassward;
  let fixture: ComponentFixture<Forgotpassward>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Forgotpassward]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Forgotpassward);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
