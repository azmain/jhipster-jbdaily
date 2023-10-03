import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pay-order.test-samples';

import { PayOrderFormService } from './pay-order-form.service';

describe('PayOrder Form Service', () => {
  let service: PayOrderFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PayOrderFormService);
  });

  describe('Service methods', () => {
    describe('createPayOrderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPayOrderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            payOrderNumber: expect.any(Object),
            payOrderDate: expect.any(Object),
            amount: expect.any(Object),
            slipNo: expect.any(Object),
            controllingNo: expect.any(Object),
            fertilizer: expect.any(Object),
            dealer: expect.any(Object),
          })
        );
      });

      it('passing IPayOrder should create a new form with FormGroup', () => {
        const formGroup = service.createPayOrderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            payOrderNumber: expect.any(Object),
            payOrderDate: expect.any(Object),
            amount: expect.any(Object),
            slipNo: expect.any(Object),
            controllingNo: expect.any(Object),
            fertilizer: expect.any(Object),
            dealer: expect.any(Object),
          })
        );
      });
    });

    describe('getPayOrder', () => {
      it('should return NewPayOrder for default PayOrder initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPayOrderFormGroup(sampleWithNewData);

        const payOrder = service.getPayOrder(formGroup) as any;

        expect(payOrder).toMatchObject(sampleWithNewData);
      });

      it('should return NewPayOrder for empty PayOrder initial value', () => {
        const formGroup = service.createPayOrderFormGroup();

        const payOrder = service.getPayOrder(formGroup) as any;

        expect(payOrder).toMatchObject({});
      });

      it('should return IPayOrder', () => {
        const formGroup = service.createPayOrderFormGroup(sampleWithRequiredData);

        const payOrder = service.getPayOrder(formGroup) as any;

        expect(payOrder).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPayOrder should not enable id FormControl', () => {
        const formGroup = service.createPayOrderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPayOrder should disable id FormControl', () => {
        const formGroup = service.createPayOrderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
