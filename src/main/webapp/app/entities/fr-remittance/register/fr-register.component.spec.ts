import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FrRegisterComponent } from './fr-register.component';

describe('FrRegisterComponent', () => {
  let component: FrRegisterComponent;
  let fixture: ComponentFixture<FrRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FrRegisterComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FrRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
