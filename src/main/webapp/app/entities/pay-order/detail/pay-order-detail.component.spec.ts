import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PayOrderDetailComponent } from './pay-order-detail.component';

describe('PayOrder Management Detail Component', () => {
  let comp: PayOrderDetailComponent;
  let fixture: ComponentFixture<PayOrderDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PayOrderDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ payOrder: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PayOrderDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PayOrderDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load payOrder on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.payOrder).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
