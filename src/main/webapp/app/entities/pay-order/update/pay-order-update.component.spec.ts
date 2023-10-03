import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PayOrderFormService } from './pay-order-form.service';
import { PayOrderService } from '../service/pay-order.service';
import { IPayOrder } from '../pay-order.model';
import { IFertilizer } from 'app/entities/fertilizer/fertilizer.model';
import { FertilizerService } from 'app/entities/fertilizer/service/fertilizer.service';
import { IDealer } from 'app/entities/dealer/dealer.model';
import { DealerService } from 'app/entities/dealer/service/dealer.service';

import { PayOrderUpdateComponent } from './pay-order-update.component';

describe('PayOrder Management Update Component', () => {
  let comp: PayOrderUpdateComponent;
  let fixture: ComponentFixture<PayOrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let payOrderFormService: PayOrderFormService;
  let payOrderService: PayOrderService;
  let fertilizerService: FertilizerService;
  let dealerService: DealerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PayOrderUpdateComponent],
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
      .overrideTemplate(PayOrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PayOrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    payOrderFormService = TestBed.inject(PayOrderFormService);
    payOrderService = TestBed.inject(PayOrderService);
    fertilizerService = TestBed.inject(FertilizerService);
    dealerService = TestBed.inject(DealerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Fertilizer query and add missing value', () => {
      const payOrder: IPayOrder = { id: 456 };
      const fertilizer: IFertilizer = { id: 48411 };
      payOrder.fertilizer = fertilizer;

      const fertilizerCollection: IFertilizer[] = [{ id: 17650 }];
      jest.spyOn(fertilizerService, 'query').mockReturnValue(of(new HttpResponse({ body: fertilizerCollection })));
      const additionalFertilizers = [fertilizer];
      const expectedCollection: IFertilizer[] = [...additionalFertilizers, ...fertilizerCollection];
      jest.spyOn(fertilizerService, 'addFertilizerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payOrder });
      comp.ngOnInit();

      expect(fertilizerService.query).toHaveBeenCalled();
      expect(fertilizerService.addFertilizerToCollectionIfMissing).toHaveBeenCalledWith(
        fertilizerCollection,
        ...additionalFertilizers.map(expect.objectContaining)
      );
      expect(comp.fertilizersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Dealer query and add missing value', () => {
      const payOrder: IPayOrder = { id: 456 };
      const dealer: IDealer = { id: 90993 };
      payOrder.dealer = dealer;

      const dealerCollection: IDealer[] = [{ id: 58695 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [dealer];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payOrder });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(
        dealerCollection,
        ...additionalDealers.map(expect.objectContaining)
      );
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const payOrder: IPayOrder = { id: 456 };
      const fertilizer: IFertilizer = { id: 74333 };
      payOrder.fertilizer = fertilizer;
      const dealer: IDealer = { id: 23500 };
      payOrder.dealer = dealer;

      activatedRoute.data = of({ payOrder });
      comp.ngOnInit();

      expect(comp.fertilizersSharedCollection).toContain(fertilizer);
      expect(comp.dealersSharedCollection).toContain(dealer);
      expect(comp.payOrder).toEqual(payOrder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPayOrder>>();
      const payOrder = { id: 123 };
      jest.spyOn(payOrderFormService, 'getPayOrder').mockReturnValue(payOrder);
      jest.spyOn(payOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: payOrder }));
      saveSubject.complete();

      // THEN
      expect(payOrderFormService.getPayOrder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(payOrderService.update).toHaveBeenCalledWith(expect.objectContaining(payOrder));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPayOrder>>();
      const payOrder = { id: 123 };
      jest.spyOn(payOrderFormService, 'getPayOrder').mockReturnValue({ id: null });
      jest.spyOn(payOrderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payOrder: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: payOrder }));
      saveSubject.complete();

      // THEN
      expect(payOrderFormService.getPayOrder).toHaveBeenCalled();
      expect(payOrderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPayOrder>>();
      const payOrder = { id: 123 };
      jest.spyOn(payOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(payOrderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFertilizer', () => {
      it('Should forward to fertilizerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(fertilizerService, 'compareFertilizer');
        comp.compareFertilizer(entity, entity2);
        expect(fertilizerService.compareFertilizer).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDealer', () => {
      it('Should forward to dealerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(dealerService, 'compareDealer');
        comp.compareDealer(entity, entity2);
        expect(dealerService.compareDealer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
