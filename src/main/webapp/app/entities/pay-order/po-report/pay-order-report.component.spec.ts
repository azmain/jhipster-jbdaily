import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PayOrderReportComponent } from './pay-order-report.component';

describe('PayOrderReportComponent', () => {
  let component: PayOrderReportComponent;
  let fixture: ComponentFixture<PayOrderReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PayOrderReportComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(PayOrderReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
