import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FrRemittanceFormService } from './fr-remittance-form.service';
import { FrRemittanceService } from '../service/fr-remittance.service';
import { IFrRemittance } from '../fr-remittance.model';
import { IMoneyExchange } from 'app/entities/money-exchange/money-exchange.model';
import { MoneyExchangeService } from 'app/entities/money-exchange/service/money-exchange.service';
import { IIncPercentage } from 'app/entities/inc-percentage/inc-percentage.model';
import { IncPercentageService } from 'app/entities/inc-percentage/service/inc-percentage.service';

import { FrRemittanceUpdateComponent } from './fr-remittance-update.component';

describe('FrRemittance Management Update Component', () => {
  let comp: FrRemittanceUpdateComponent;
  let fixture: ComponentFixture<FrRemittanceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let frRemittanceFormService: FrRemittanceFormService;
  let frRemittanceService: FrRemittanceService;
  let moneyExchangeService: MoneyExchangeService;
  let incPercentageService: IncPercentageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FrRemittanceUpdateComponent],
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
      .overrideTemplate(FrRemittanceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FrRemittanceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    frRemittanceFormService = TestBed.inject(FrRemittanceFormService);
    frRemittanceService = TestBed.inject(FrRemittanceService);
    moneyExchangeService = TestBed.inject(MoneyExchangeService);
    incPercentageService = TestBed.inject(IncPercentageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MoneyExchange query and add missing value', () => {
      const frRemittance: IFrRemittance = { id: 456 };
      const moneyExchange: IMoneyExchange = { id: 33783 };
      frRemittance.moneyExchange = moneyExchange;

      const moneyExchangeCollection: IMoneyExchange[] = [{ id: 27567 }];
      jest.spyOn(moneyExchangeService, 'query').mockReturnValue(of(new HttpResponse({ body: moneyExchangeCollection })));
      const additionalMoneyExchanges = [moneyExchange];
      const expectedCollection: IMoneyExchange[] = [...additionalMoneyExchanges, ...moneyExchangeCollection];
      jest.spyOn(moneyExchangeService, 'addMoneyExchangeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ frRemittance });
      comp.ngOnInit();

      expect(moneyExchangeService.query).toHaveBeenCalled();
      expect(moneyExchangeService.addMoneyExchangeToCollectionIfMissing).toHaveBeenCalledWith(
        moneyExchangeCollection,
        ...additionalMoneyExchanges.map(expect.objectContaining)
      );
      expect(comp.moneyExchangesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call IncPercentage query and add missing value', () => {
      const frRemittance: IFrRemittance = { id: 456 };
      const incPercentage: IIncPercentage = { id: 54565 };
      frRemittance.incPercentage = incPercentage;

      const incPercentageCollection: IIncPercentage[] = [{ id: 12904 }];
      jest.spyOn(incPercentageService, 'query').mockReturnValue(of(new HttpResponse({ body: incPercentageCollection })));
      const additionalIncPercentages = [incPercentage];
      const expectedCollection: IIncPercentage[] = [...additionalIncPercentages, ...incPercentageCollection];
      jest.spyOn(incPercentageService, 'addIncPercentageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ frRemittance });
      comp.ngOnInit();

      expect(incPercentageService.query).toHaveBeenCalled();
      expect(incPercentageService.addIncPercentageToCollectionIfMissing).toHaveBeenCalledWith(
        incPercentageCollection,
        ...additionalIncPercentages.map(expect.objectContaining)
      );
      expect(comp.incPercentagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const frRemittance: IFrRemittance = { id: 456 };
      const moneyExchange: IMoneyExchange = { id: 62194 };
      frRemittance.moneyExchange = moneyExchange;
      const incPercentage: IIncPercentage = { id: 5488 };
      frRemittance.incPercentage = incPercentage;

      activatedRoute.data = of({ frRemittance });
      comp.ngOnInit();

      expect(comp.moneyExchangesSharedCollection).toContain(moneyExchange);
      expect(comp.incPercentagesSharedCollection).toContain(incPercentage);
      expect(comp.frRemittance).toEqual(frRemittance);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFrRemittance>>();
      const frRemittance = { id: 123 };
      jest.spyOn(frRemittanceFormService, 'getFrRemittance').mockReturnValue(frRemittance);
      jest.spyOn(frRemittanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ frRemittance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: frRemittance }));
      saveSubject.complete();

      // THEN
      expect(frRemittanceFormService.getFrRemittance).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(frRemittanceService.update).toHaveBeenCalledWith(expect.objectContaining(frRemittance));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFrRemittance>>();
      const frRemittance = { id: 123 };
      jest.spyOn(frRemittanceFormService, 'getFrRemittance').mockReturnValue({ id: null });
      jest.spyOn(frRemittanceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ frRemittance: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: frRemittance }));
      saveSubject.complete();

      // THEN
      expect(frRemittanceFormService.getFrRemittance).toHaveBeenCalled();
      expect(frRemittanceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFrRemittance>>();
      const frRemittance = { id: 123 };
      jest.spyOn(frRemittanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ frRemittance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(frRemittanceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMoneyExchange', () => {
      it('Should forward to moneyExchangeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(moneyExchangeService, 'compareMoneyExchange');
        comp.compareMoneyExchange(entity, entity2);
        expect(moneyExchangeService.compareMoneyExchange).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareIncPercentage', () => {
      it('Should forward to incPercentageService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(incPercentageService, 'compareIncPercentage');
        comp.compareIncPercentage(entity, entity2);
        expect(incPercentageService.compareIncPercentage).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
