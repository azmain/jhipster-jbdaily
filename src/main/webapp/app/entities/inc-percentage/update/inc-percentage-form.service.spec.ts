import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../inc-percentage.test-samples';

import { IncPercentageFormService } from './inc-percentage-form.service';

describe('IncPercentage Form Service', () => {
  let service: IncPercentageFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IncPercentageFormService);
  });

  describe('Service methods', () => {
    describe('createIncPercentageFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIncPercentageFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });

      it('passing IIncPercentage should create a new form with FormGroup', () => {
        const formGroup = service.createIncPercentageFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });
    });

    describe('getIncPercentage', () => {
      it('should return NewIncPercentage for default IncPercentage initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createIncPercentageFormGroup(sampleWithNewData);

        const incPercentage = service.getIncPercentage(formGroup) as any;

        expect(incPercentage).toMatchObject(sampleWithNewData);
      });

      it('should return NewIncPercentage for empty IncPercentage initial value', () => {
        const formGroup = service.createIncPercentageFormGroup();

        const incPercentage = service.getIncPercentage(formGroup) as any;

        expect(incPercentage).toMatchObject({});
      });

      it('should return IIncPercentage', () => {
        const formGroup = service.createIncPercentageFormGroup(sampleWithRequiredData);

        const incPercentage = service.getIncPercentage(formGroup) as any;

        expect(incPercentage).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIncPercentage should not enable id FormControl', () => {
        const formGroup = service.createIncPercentageFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIncPercentage should disable id FormControl', () => {
        const formGroup = service.createIncPercentageFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
