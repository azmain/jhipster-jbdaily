import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UpazilaFormService } from './upazila-form.service';
import { UpazilaService } from '../service/upazila.service';
import { IUpazila } from '../upazila.model';
import { IDistrict } from 'app/entities/district/district.model';
import { DistrictService } from 'app/entities/district/service/district.service';

import { UpazilaUpdateComponent } from './upazila-update.component';

describe('Upazila Management Update Component', () => {
  let comp: UpazilaUpdateComponent;
  let fixture: ComponentFixture<UpazilaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let upazilaFormService: UpazilaFormService;
  let upazilaService: UpazilaService;
  let districtService: DistrictService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UpazilaUpdateComponent],
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
      .overrideTemplate(UpazilaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UpazilaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    upazilaFormService = TestBed.inject(UpazilaFormService);
    upazilaService = TestBed.inject(UpazilaService);
    districtService = TestBed.inject(DistrictService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call District query and add missing value', () => {
      const upazila: IUpazila = { id: 456 };
      const district: IDistrict = { id: 42336 };
      upazila.district = district;

      const districtCollection: IDistrict[] = [{ id: 64542 }];
      jest.spyOn(districtService, 'query').mockReturnValue(of(new HttpResponse({ body: districtCollection })));
      const additionalDistricts = [district];
      const expectedCollection: IDistrict[] = [...additionalDistricts, ...districtCollection];
      jest.spyOn(districtService, 'addDistrictToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ upazila });
      comp.ngOnInit();

      expect(districtService.query).toHaveBeenCalled();
      expect(districtService.addDistrictToCollectionIfMissing).toHaveBeenCalledWith(
        districtCollection,
        ...additionalDistricts.map(expect.objectContaining)
      );
      expect(comp.districtsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const upazila: IUpazila = { id: 456 };
      const district: IDistrict = { id: 72206 };
      upazila.district = district;

      activatedRoute.data = of({ upazila });
      comp.ngOnInit();

      expect(comp.districtsSharedCollection).toContain(district);
      expect(comp.upazila).toEqual(upazila);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUpazila>>();
      const upazila = { id: 123 };
      jest.spyOn(upazilaFormService, 'getUpazila').mockReturnValue(upazila);
      jest.spyOn(upazilaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ upazila });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: upazila }));
      saveSubject.complete();

      // THEN
      expect(upazilaFormService.getUpazila).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(upazilaService.update).toHaveBeenCalledWith(expect.objectContaining(upazila));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUpazila>>();
      const upazila = { id: 123 };
      jest.spyOn(upazilaFormService, 'getUpazila').mockReturnValue({ id: null });
      jest.spyOn(upazilaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ upazila: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: upazila }));
      saveSubject.complete();

      // THEN
      expect(upazilaFormService.getUpazila).toHaveBeenCalled();
      expect(upazilaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUpazila>>();
      const upazila = { id: 123 };
      jest.spyOn(upazilaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ upazila });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(upazilaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDistrict', () => {
      it('Should forward to districtService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(districtService, 'compareDistrict');
        comp.compareDistrict(entity, entity2);
        expect(districtService.compareDistrict).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
