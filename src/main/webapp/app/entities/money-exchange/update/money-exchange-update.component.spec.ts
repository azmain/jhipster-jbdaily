import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MoneyExchangeFormService } from './money-exchange-form.service';
import { MoneyExchangeService } from '../service/money-exchange.service';
import { IMoneyExchange } from '../money-exchange.model';

import { MoneyExchangeUpdateComponent } from './money-exchange-update.component';

describe('MoneyExchange Management Update Component', () => {
  let comp: MoneyExchangeUpdateComponent;
  let fixture: ComponentFixture<MoneyExchangeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let moneyExchangeFormService: MoneyExchangeFormService;
  let moneyExchangeService: MoneyExchangeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MoneyExchangeUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MoneyExchangeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MoneyExchangeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    moneyExchangeFormService = TestBed.inject(MoneyExchangeFormService);
    moneyExchangeService = TestBed.inject(MoneyExchangeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const moneyExchange: IMoneyExchange = { id: 456 };

      activatedRoute.data = of({ moneyExchange });
      comp.ngOnInit();

      expect(comp.moneyExchange).toEqual(moneyExchange);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoneyExchange>>();
      const moneyExchange = { id: 123 };
      jest.spyOn(moneyExchangeFormService, 'getMoneyExchange').mockReturnValue(moneyExchange);
      jest.spyOn(moneyExchangeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moneyExchange });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moneyExchange }));
      saveSubject.complete();

      // THEN
      expect(moneyExchangeFormService.getMoneyExchange).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(moneyExchangeService.update).toHaveBeenCalledWith(expect.objectContaining(moneyExchange));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoneyExchange>>();
      const moneyExchange = { id: 123 };
      jest.spyOn(moneyExchangeFormService, 'getMoneyExchange').mockReturnValue({ id: null });
      jest.spyOn(moneyExchangeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moneyExchange: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moneyExchange }));
      saveSubject.complete();

      // THEN
      expect(moneyExchangeFormService.getMoneyExchange).toHaveBeenCalled();
      expect(moneyExchangeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoneyExchange>>();
      const moneyExchange = { id: 123 };
      jest.spyOn(moneyExchangeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moneyExchange });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(moneyExchangeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
