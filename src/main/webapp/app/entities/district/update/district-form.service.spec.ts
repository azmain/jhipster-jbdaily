import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../district.test-samples';

import { DistrictFormService } from './district-form.service';

describe('District Form Service', () => {
  let service: DistrictFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DistrictFormService);
  });

  describe('Service methods', () => {
    describe('createDistrictFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDistrictFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            bnName: expect.any(Object),
            division: expect.any(Object),
          })
        );
      });

      it('passing IDistrict should create a new form with FormGroup', () => {
        const formGroup = service.createDistrictFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            bnName: expect.any(Object),
            division: expect.any(Object),
          })
        );
      });
    });

    describe('getDistrict', () => {
      it('should return NewDistrict for default District initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDistrictFormGroup(sampleWithNewData);

        const district = service.getDistrict(formGroup) as any;

        expect(district).toMatchObject(sampleWithNewData);
      });

      it('should return NewDistrict for empty District initial value', () => {
        const formGroup = service.createDistrictFormGroup();

        const district = service.getDistrict(formGroup) as any;

        expect(district).toMatchObject({});
      });

      it('should return IDistrict', () => {
        const formGroup = service.createDistrictFormGroup(sampleWithRequiredData);

        const district = service.getDistrict(formGroup) as any;

        expect(district).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDistrict should not enable id FormControl', () => {
        const formGroup = service.createDistrictFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDistrict should disable id FormControl', () => {
        const formGroup = service.createDistrictFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
