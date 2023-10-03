import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DealerFormService } from './dealer-form.service';
import { DealerService } from '../service/dealer.service';
import { IDealer } from '../dealer.model';
import { IUpazila } from 'app/entities/upazila/upazila.model';
import { UpazilaService } from 'app/entities/upazila/service/upazila.service';

import { DealerUpdateComponent } from './dealer-update.component';

describe('Dealer Management Update Component', () => {
  let comp: DealerUpdateComponent;
  let fixture: ComponentFixture<DealerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dealerFormService: DealerFormService;
  let dealerService: DealerService;
  let upazilaService: UpazilaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DealerUpdateComponent],
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
      .overrideTemplate(DealerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DealerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dealerFormService = TestBed.inject(DealerFormService);
    dealerService = TestBed.inject(DealerService);
    upazilaService = TestBed.inject(UpazilaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Upazila query and add missing value', () => {
      const dealer: IDealer = { id: 456 };
      const upazila: IUpazila = { id: 35121 };
      dealer.upazila = upazila;

      const upazilaCollection: IUpazila[] = [{ id: 14979 }];
      jest.spyOn(upazilaService, 'query').mockReturnValue(of(new HttpResponse({ body: upazilaCollection })));
      const additionalUpazilas = [upazila];
      const expectedCollection: IUpazila[] = [...additionalUpazilas, ...upazilaCollection];
      jest.spyOn(upazilaService, 'addUpazilaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dealer });
      comp.ngOnInit();

      expect(upazilaService.query).toHaveBeenCalled();
      expect(upazilaService.addUpazilaToCollectionIfMissing).toHaveBeenCalledWith(
        upazilaCollection,
        ...additionalUpazilas.map(expect.objectContaining)
      );
      expect(comp.upazilasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const dealer: IDealer = { id: 456 };
      const upazila: IUpazila = { id: 5520 };
      dealer.upazila = upazila;

      activatedRoute.data = of({ dealer });
      comp.ngOnInit();

      expect(comp.upazilasSharedCollection).toContain(upazila);
      expect(comp.dealer).toEqual(dealer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDealer>>();
      const dealer = { id: 123 };
      jest.spyOn(dealerFormService, 'getDealer').mockReturnValue(dealer);
      jest.spyOn(dealerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dealer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dealer }));
      saveSubject.complete();

      // THEN
      expect(dealerFormService.getDealer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dealerService.update).toHaveBeenCalledWith(expect.objectContaining(dealer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDealer>>();
      const dealer = { id: 123 };
      jest.spyOn(dealerFormService, 'getDealer').mockReturnValue({ id: null });
      jest.spyOn(dealerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dealer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dealer }));
      saveSubject.complete();

      // THEN
      expect(dealerFormService.getDealer).toHaveBeenCalled();
      expect(dealerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDealer>>();
      const dealer = { id: 123 };
      jest.spyOn(dealerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dealer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dealerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUpazila', () => {
      it('Should forward to upazilaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(upazilaService, 'compareUpazila');
        comp.compareUpazila(entity, entity2);
        expect(upazilaService.compareUpazila).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
