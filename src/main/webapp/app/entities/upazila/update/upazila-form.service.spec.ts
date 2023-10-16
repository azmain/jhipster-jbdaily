import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../upazila.test-samples';

import { UpazilaFormService } from './upazila-form.service';

describe('Upazila Form Service', () => {
  let service: UpazilaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UpazilaFormService);
  });

  describe('Service methods', () => {
    describe('createUpazilaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUpazilaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            bnName: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            district: expect.any(Object),
          })
        );
      });

      it('passing IUpazila should create a new form with FormGroup', () => {
        const formGroup = service.createUpazilaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            bnName: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            district: expect.any(Object),
          })
        );
      });
    });

    describe('getUpazila', () => {
      it('should return NewUpazila for default Upazila initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createUpazilaFormGroup(sampleWithNewData);

        const upazila = service.getUpazila(formGroup) as any;

        expect(upazila).toMatchObject(sampleWithNewData);
      });

      it('should return NewUpazila for empty Upazila initial value', () => {
        const formGroup = service.createUpazilaFormGroup();

        const upazila = service.getUpazila(formGroup) as any;

        expect(upazila).toMatchObject({});
      });

      it('should return IUpazila', () => {
        const formGroup = service.createUpazilaFormGroup(sampleWithRequiredData);

        const upazila = service.getUpazila(formGroup) as any;

        expect(upazila).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUpazila should not enable id FormControl', () => {
        const formGroup = service.createUpazilaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUpazila should disable id FormControl', () => {
        const formGroup = service.createUpazilaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
