import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../fertilizer.test-samples';

import { FertilizerFormService } from './fertilizer-form.service';

describe('Fertilizer Form Service', () => {
  let service: FertilizerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FertilizerFormService);
  });

  describe('Service methods', () => {
    describe('createFertilizerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFertilizerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            bnName: expect.any(Object),
            accountNo: expect.any(Object),
            accountTitle: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          })
        );
      });

      it('passing IFertilizer should create a new form with FormGroup', () => {
        const formGroup = service.createFertilizerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            bnName: expect.any(Object),
            accountNo: expect.any(Object),
            accountTitle: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          })
        );
      });
    });

    describe('getFertilizer', () => {
      it('should return NewFertilizer for default Fertilizer initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFertilizerFormGroup(sampleWithNewData);

        const fertilizer = service.getFertilizer(formGroup) as any;

        expect(fertilizer).toMatchObject(sampleWithNewData);
      });

      it('should return NewFertilizer for empty Fertilizer initial value', () => {
        const formGroup = service.createFertilizerFormGroup();

        const fertilizer = service.getFertilizer(formGroup) as any;

        expect(fertilizer).toMatchObject({});
      });

      it('should return IFertilizer', () => {
        const formGroup = service.createFertilizerFormGroup(sampleWithRequiredData);

        const fertilizer = service.getFertilizer(formGroup) as any;

        expect(fertilizer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFertilizer should not enable id FormControl', () => {
        const formGroup = service.createFertilizerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFertilizer should disable id FormControl', () => {
        const formGroup = service.createFertilizerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
