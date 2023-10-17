import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PayOrderMicrComponentComponent } from './pay-order-micr-component.component';

describe('PayOrderMicrComponentComponent', () => {
  let component: PayOrderMicrComponentComponent;
  let fixture: ComponentFixture<PayOrderMicrComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PayOrderMicrComponentComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(PayOrderMicrComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
