import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MoneyExchangeDetailComponent } from './money-exchange-detail.component';

describe('MoneyExchange Management Detail Component', () => {
  let comp: MoneyExchangeDetailComponent;
  let fixture: ComponentFixture<MoneyExchangeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MoneyExchangeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ moneyExchange: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MoneyExchangeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MoneyExchangeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load moneyExchange on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.moneyExchange).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
