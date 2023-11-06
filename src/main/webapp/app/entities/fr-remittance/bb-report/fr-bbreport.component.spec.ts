import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BBReportComponent } from './fr-bbreport.component';

describe('BBReportComponent', () => {
  let component: BBReportComponent;
  let fixture: ComponentFixture<BBReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BBReportComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(BBReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
