import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IncPercentageFormService } from './inc-percentage-form.service';
import { IncPercentageService } from '../service/inc-percentage.service';
import { IIncPercentage } from '../inc-percentage.model';

import { IncPercentageUpdateComponent } from './inc-percentage-update.component';

describe('IncPercentage Management Update Component', () => {
  let comp: IncPercentageUpdateComponent;
  let fixture: ComponentFixture<IncPercentageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let incPercentageFormService: IncPercentageFormService;
  let incPercentageService: IncPercentageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IncPercentageUpdateComponent],
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
      .overrideTemplate(IncPercentageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IncPercentageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    incPercentageFormService = TestBed.inject(IncPercentageFormService);
    incPercentageService = TestBed.inject(IncPercentageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const incPercentage: IIncPercentage = { id: 456 };

      activatedRoute.data = of({ incPercentage });
      comp.ngOnInit();

      expect(comp.incPercentage).toEqual(incPercentage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIncPercentage>>();
      const incPercentage = { id: 123 };
      jest.spyOn(incPercentageFormService, 'getIncPercentage').mockReturnValue(incPercentage);
      jest.spyOn(incPercentageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incPercentage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: incPercentage }));
      saveSubject.complete();

      // THEN
      expect(incPercentageFormService.getIncPercentage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(incPercentageService.update).toHaveBeenCalledWith(expect.objectContaining(incPercentage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIncPercentage>>();
      const incPercentage = { id: 123 };
      jest.spyOn(incPercentageFormService, 'getIncPercentage').mockReturnValue({ id: null });
      jest.spyOn(incPercentageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incPercentage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: incPercentage }));
      saveSubject.complete();

      // THEN
      expect(incPercentageFormService.getIncPercentage).toHaveBeenCalled();
      expect(incPercentageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIncPercentage>>();
      const incPercentage = { id: 123 };
      jest.spyOn(incPercentageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incPercentage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(incPercentageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
