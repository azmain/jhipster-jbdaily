import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FertilizerFormService } from './fertilizer-form.service';
import { FertilizerService } from '../service/fertilizer.service';
import { IFertilizer } from '../fertilizer.model';

import { FertilizerUpdateComponent } from './fertilizer-update.component';

describe('Fertilizer Management Update Component', () => {
  let comp: FertilizerUpdateComponent;
  let fixture: ComponentFixture<FertilizerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fertilizerFormService: FertilizerFormService;
  let fertilizerService: FertilizerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FertilizerUpdateComponent],
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
      .overrideTemplate(FertilizerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FertilizerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fertilizerFormService = TestBed.inject(FertilizerFormService);
    fertilizerService = TestBed.inject(FertilizerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fertilizer: IFertilizer = { id: 456 };

      activatedRoute.data = of({ fertilizer });
      comp.ngOnInit();

      expect(comp.fertilizer).toEqual(fertilizer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFertilizer>>();
      const fertilizer = { id: 123 };
      jest.spyOn(fertilizerFormService, 'getFertilizer').mockReturnValue(fertilizer);
      jest.spyOn(fertilizerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fertilizer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fertilizer }));
      saveSubject.complete();

      // THEN
      expect(fertilizerFormService.getFertilizer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fertilizerService.update).toHaveBeenCalledWith(expect.objectContaining(fertilizer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFertilizer>>();
      const fertilizer = { id: 123 };
      jest.spyOn(fertilizerFormService, 'getFertilizer').mockReturnValue({ id: null });
      jest.spyOn(fertilizerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fertilizer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fertilizer }));
      saveSubject.complete();

      // THEN
      expect(fertilizerFormService.getFertilizer).toHaveBeenCalled();
      expect(fertilizerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFertilizer>>();
      const fertilizer = { id: 123 };
      jest.spyOn(fertilizerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fertilizer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fertilizerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
